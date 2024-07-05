package com.zzyl.security;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzyl.constant.SecurityConstant;
import com.zzyl.constant.UserCacheConstant;
import com.zzyl.properties.JwtTokenManagerProperties;
import com.zzyl.utils.EmptyUtil;
import com.zzyl.utils.JwtUtil;
import com.zzyl.utils.UserThreadLocal;
import com.zzyl.vo.UserVo;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 *  授权管理器
 */
@Slf4j
@Component
public class JwtAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JwtTokenManagerProperties jwtTokenManagerProperties;


    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication,
                                       RequestAuthorizationContext requestAuthorizationContext) {
        //用户当前请求路径
        String method = requestAuthorizationContext.getRequest().getMethod();
        String requestURI = requestAuthorizationContext.getRequest().getRequestURI();
        String targetUrl = (method+requestURI);

        //获得请求中的认证后传递过来的userToken
        String userToken = requestAuthorizationContext.getRequest().getHeader(SecurityConstant.USER_TOKEN);

        //如果userToken为空,则当前请求不合法
        if (EmptyUtil.isNullOrEmpty(userToken)){
            return new AuthorizationDecision(false);
        }

        //通过userToken获取jwtToken
        String jwtTokenKey = UserCacheConstant.JWT_TOKEN+userToken;
        //key:uuid
        String jwtToken = redisTemplate.opsForValue().get(jwtTokenKey);

        //如果jwtToken为空,则当前请求不合法
        if (EmptyUtil.isNullOrEmpty(jwtToken)){
            return new AuthorizationDecision(false);
        }

        //校验jwtToken是否合法
        Claims cla = JwtUtil.parseJWT(jwtTokenManagerProperties.getBase64EncodedSecretKey(), jwtToken);
        if (ObjectUtil.isEmpty(cla)) {
            //token失效
            return new AuthorizationDecision(false);
        }

        //如果校验jwtToken通过，则获得userVo对象
        UserVo userVo = JSONObject.parseObject(String.valueOf(cla.get("currentUser")),UserVo.class);

        //用户剔除校验:redis中最新的userToken与出入的userToken不符合，则认为当前用户被后续用户剔除
        //key:username  value：uuid
        String currentUserToken = redisTemplate.opsForValue().get(UserCacheConstant.USER_TOKEN + userVo.getUsername());
        if (!userToken.equals(currentUserToken)){
            return new AuthorizationDecision(false);
        }

        //如果当前UserToken存活时间少于10分钟，则进行续期
        Long remainTimeToLive = redisTemplate.opsForValue().getOperations().getExpire(jwtTokenKey);
        if (remainTimeToLive.longValue()<= 600){
            //jwt生成的token也会过期，所以需要重新生成jwttoken
            Map<String, Object> claims = new HashMap<>();
            String userVoJsonString = String.valueOf(cla.get("currentUser"));
            claims.put("currentUser", userVoJsonString);

            //jwtToken令牌颁布
            String newJwtToken = JwtUtil.createJWT(jwtTokenManagerProperties.getBase64EncodedSecretKey(), jwtTokenManagerProperties.getTtl(), claims);
            long ttl = Long.valueOf(jwtTokenManagerProperties.getTtl()) / 1000;

            redisTemplate.opsForValue().set(jwtTokenKey, newJwtToken, ttl, TimeUnit.SECONDS);
            redisTemplate.expire(UserCacheConstant.USER_TOKEN + userVo.getUsername(), ttl, TimeUnit.SECONDS);
        }

        //当前用户资源是否包含当前URL
        /*for (String resourceRequestPath : userVo.getResourceRequestPaths()) {
            boolean isMatch = antPathMatcher.match(resourceRequestPath, targetUrl);
            if (isMatch){
                log.info("用户:{}拥有targetUrl权限:{}==========",userVo.getUsername(),targetUrl);
                return new AuthorizationDecision(true);
            }
        }*/

        return new AuthorizationDecision(true);
    }

}
