package com.zzyl.security;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
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

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JwtTokenManagerProperties jwtTokenManagerProperties;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        HttpServletRequest request = object.getRequest();
        // 从请求中获取token
        String token = request.getHeader(SecurityConstant.USER_TOKEN);
        if (ObjectUtil.isEmpty(token))
            return new AuthorizationDecision(false);


        String tokenKey = UserCacheConstant.JWT_TOKEN + token;

        // 从 redis 中获取jwtToken
        String jwtToken = redisTemplate.opsForValue().get(tokenKey);

        if (ObjectUtil.isEmpty(jwtToken))
            return new AuthorizationDecision(false);

        // 解析 jwtToken
        String secretKey = jwtTokenManagerProperties.getBase64EncodedSecretKey();
        Claims claims = JwtUtil.parseJWT(secretKey, jwtToken);

        if (ObjectUtil.isEmpty(claims))
            return new AuthorizationDecision(false);

        String userObject = String.valueOf(claims.get(UserCacheConstant.CACHE));
//        UserVo userVo = JSONUtil.toBean(userObject, UserVo.class);
        UserVo userVo = JSONObject.parseObject(userObject, UserVo.class);

        // 获取用户 Key
        String username = userVo.getUsername();
        String userKey = UserCacheConstant.USER_TOKEN + username;

        // 前端传过来的 key 与 redis 中的key比较
        String currentToken = redisTemplate.opsForValue().get(userKey);
        if (!token.equals(currentToken))
            // uuid 过期
            return new AuthorizationDecision(false);

        // 判断过期时间是否小于10分钟
        Long expire = redisTemplate.opsForValue().getOperations().getExpire(tokenKey);
        if (expire != null && expire <= 600)
        {
            // 进行续期, jwt 的 token, uuid 的 token
            Map<String, Object> newClaims = new HashMap<>();
            newClaims.put(UserCacheConstant.CACHE,JSONObject.toJSONString(userVo));

            // jwt 生成
            Integer ttl = jwtTokenManagerProperties.getTtl();
            String newJwtToken = JwtUtil.createJWT(secretKey, ttl, newClaims);
            redisTemplate.opsForValue().set(tokenKey,newJwtToken,ttl,TimeUnit.MILLISECONDS);

            // token 续期
            redisTemplate.expire(userKey,ttl,TimeUnit.MILLISECONDS);
        }

        // 当前用户资源是否包含URL
        // 当前的请求方式+请求路径（拼接）
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String res = method + requestURI;
        for (String resource : userVo.getResourceRequestPaths()) {

            boolean match = antPathMatcher.match(resource, res);
            if (match)
                return new AuthorizationDecision(true);
        }
        return new AuthorizationDecision(true);
    }



    //    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//    @Autowired
//    private JwtTokenManagerProperties jwtTokenManagerProperties;
//
//
//    @Override
//    public AuthorizationDecision check(Supplier<Authentication> authentication,
//                                       RequestAuthorizationContext requestAuthorizationContext) {
//        //用户当前请求路径
//        String method = requestAuthorizationContext.getRequest().getMethod();
//        String requestURI = requestAuthorizationContext.getRequest().getRequestURI();
//        String targetUrl = (method+requestURI);
//
//        //获得请求中的认证后传递过来的userToken
//        String userToken = requestAuthorizationContext.getRequest().getHeader(SecurityConstant.USER_TOKEN);
//
//        //如果userToken为空,则当前请求不合法
//        if (EmptyUtil.isNullOrEmpty(userToken)){
//            return new AuthorizationDecision(false);
//        }
//
//        //通过userToken获取jwtToken
//        String jwtTokenKey = UserCacheConstant.JWT_TOKEN+userToken;
//        //key:uuid
//        String jwtToken = redisTemplate.opsForValue().get(jwtTokenKey);
//
//        //如果jwtToken为空,则当前请求不合法
//        if (EmptyUtil.isNullOrEmpty(jwtToken)){
//            return new AuthorizationDecision(false);
//        }
//
//        //校验jwtToken是否合法
//        Claims cla = JwtUtil.parseJWT(jwtTokenManagerProperties.getBase64EncodedSecretKey(), jwtToken);
//        if (ObjectUtil.isEmpty(cla)) {
//            //token失效
//            return new AuthorizationDecision(false);
//        }
//
//        //如果校验jwtToken通过，则获得userVo对象
//        UserVo userVo = JSONObject.parseObject(String.valueOf(cla.get("currentUser")),UserVo.class);
//
//        //用户剔除校验:redis中最新的userToken与出入的userToken不符合，则认为当前用户被后续用户剔除
//        //key:username  value：uuid
//        String currentUserToken = redisTemplate.opsForValue().get(UserCacheConstant.USER_TOKEN + userVo.getUsername());
//        if (!userToken.equals(currentUserToken)){
//            return new AuthorizationDecision(false);
//        }
//
//        //如果当前UserToken存活时间少于10分钟，则进行续期
//        Long remainTimeToLive = redisTemplate.opsForValue().getOperations().getExpire(jwtTokenKey);
//        if (remainTimeToLive.longValue()<= 600){
//            //jwt生成的token也会过期，所以需要重新生成jwttoken
//            Map<String, Object> claims = new HashMap<>();
//            String userVoJsonString = String.valueOf(cla.get("currentUser"));
//            claims.put("currentUser", userVoJsonString);
//
//            //jwtToken令牌颁布
//            String newJwtToken = JwtUtil.createJWT(jwtTokenManagerProperties.getBase64EncodedSecretKey(), jwtTokenManagerProperties.getTtl(), claims);
//            long ttl = Long.valueOf(jwtTokenManagerProperties.getTtl()) / 1000;
//
//            redisTemplate.opsForValue().set(jwtTokenKey, newJwtToken, ttl, TimeUnit.SECONDS);
//            redisTemplate.expire(UserCacheConstant.USER_TOKEN + userVo.getUsername(), ttl, TimeUnit.SECONDS);
//        }
//
//        //当前用户资源是否包含当前URL
//        /*for (String resourceRequestPath : userVo.getResourceRequestPaths()) {
//            boolean isMatch = antPathMatcher.match(resourceRequestPath, targetUrl);
//            if (isMatch){
//                log.info("用户:{}拥有targetUrl权限:{}==========",userVo.getUsername(),targetUrl);
//                return new AuthorizationDecision(true);
//            }
//        }*/
//
//        return new AuthorizationDecision(true);
//    }

}
