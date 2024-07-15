package com.zzyl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.zzyl.constant.UserCacheConstant;
import com.zzyl.dto.LoginDto;
import com.zzyl.exception.BaseException;
import com.zzyl.properties.JwtTokenManagerProperties;
import com.zzyl.service.LoginService;
import com.zzyl.service.ResourceService;
import com.zzyl.service.RoleDeptService;
import com.zzyl.service.RoleService;
import com.zzyl.utils.BeanConv;
import com.zzyl.utils.JwtUtil;
import com.zzyl.vo.ResourceVo;
import com.zzyl.vo.RoleVo;
import com.zzyl.vo.UserAuth;
import com.zzyl.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 登录接口实现
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleService roleService;

    @Autowired
    ResourceService resourceService;

    @Autowired
    private JwtTokenManagerProperties jwtTokenManagerProperties;

    @Autowired
    RoleDeptService roleDeptService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public UserVo login(LoginDto loginDto) {
        // 使用认证管理器来进行认证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if (!authenticate.isAuthenticated()) {
            throw new BaseException("用户登录失败");
        }

        // 获取用户信息
        UserAuth userAuth = (UserAuth) authenticate.getPrincipal();
        UserVo userVo = BeanUtil.toBean(userAuth, UserVo.class);

        Long userId = userVo.getId();

        // 获取用户资源列表
        List<ResourceVo> resourceVos =
                resourceService.findResourceVoListByUserId(userId);

        Set<String> resources = resourceVos.stream()
                .filter(r -> "r".equals(r.getResourceType()))
                .map(ResourceVo::getRequestPath)
                .collect(Collectors.toSet());

        userVo.setResourceRequestPaths(resources);

        // 获取用户角色列表
        List<RoleVo> roleVoList = roleService.findRoleVoListByUserId(userId);
        Set<String> roles = roleVoList.stream().map(RoleVo::getLabel).collect(Collectors.toSet());
        userVo.setRoleLabels(roles);
        userVo.setRoleList(roleVoList);

        // 脱敏
        userVo.setPassword("");

        // 生成uuid
        String token = UUID.randomUUID().toString();
        userVo.setUserToken(token);

        // 获取过期时间
        Integer ttl = jwtTokenManagerProperties.getTtl();

        // 存储username到redis中
        String username = userVo.getUsername();
        String userToken = UserCacheConstant.USER_TOKEN + username;
        redisTemplate.opsForValue().set(userToken,token,ttl,TimeUnit.MILLISECONDS);

        // 存储 jwt 到 redis 中
        String secretKey = jwtTokenManagerProperties.getBase64EncodedSecretKey();
        Map<String, Object> claims = new HashMap<>();
        claims.put(UserCacheConstant.CACHE, JSONObject.toJSONString(userVo));

        String jwtToken = JwtUtil.createJWT(secretKey, ttl, claims);
        String tokenKey = UserCacheConstant.JWT_TOKEN + token;

        redisTemplate.opsForValue().set(tokenKey,jwtToken,ttl,TimeUnit.MILLISECONDS);

        return userVo;
    }

    //    @Override
//    public UserVo login(UserVo userVo) {
//        //用户认证
//        UsernamePasswordAuthenticationToken authentication =
//                new UsernamePasswordAuthenticationToken(userVo.getUsername(), userVo.getPassword());
//        Authentication authenticate = authenticationManager.authenticate(authentication);
//
//        //认证Authentication对象获得内置对象UserAuth
//        UserAuth userAuth = (UserAuth) authenticate.getPrincipal();
//        userAuth.setPassword("");
//        UserVo userVoResult = BeanConv.toBean(userAuth, UserVo.class);
//
//        //查询当前用户拥有的资源列表，方便授权过滤器进行校验
//        List<ResourceVo> resourceVoList = resourceService.findResourceVoListByUserId(userVoResult.getId());
//        Set<String> requestPaths = resourceVoList.stream()
//                .filter(x -> "r".equals(x.getResourceType()))
//                .map(ResourceVo::getRequestPath)
//                .collect(Collectors.toSet());
//        userVoResult.setResourceRequestPaths(requestPaths);
//
//        //用户当前角色列表
//        List<RoleVo> roleVoList = roleService.findRoleVoListByUserId(userVoResult.getId());
//        Set<String> roleLabels = roleVoList
//                .stream()
//                .map(RoleVo::getLabel)
//                .collect(Collectors.toSet());
//        userVoResult.setRoleLabels(roleLabels);
//
//        //userToken令牌颁布
//        String userToken = UUID.randomUUID().toString();
//        userVoResult.setUserToken(userToken);
//
//        //构建载荷
//        Map<String, Object> claims = new HashMap<>();
//        String userVoJsonString = JSONObject.toJSONString(userVoResult);
//        claims.put("currentUser", userVoJsonString);
//
//        //jwtToken令牌颁布
//        String jwtToken = JwtUtil.createJWT(jwtTokenManagerProperties.getBase64EncodedSecretKey(), jwtTokenManagerProperties.getTtl(), claims);
//
//        //剔除缓存：用户关联userToken
//        String userTokenKey = UserCacheConstant.USER_TOKEN + userVo.getUsername();
//        long ttl = Long.valueOf(jwtTokenManagerProperties.getTtl()) / 1000;
//        //key：username   value:uuid
//        redisTemplate.opsForValue().set(userTokenKey, userToken, ttl, TimeUnit.SECONDS);
//
//        //续期缓存：userToken关联jwtToken
//        String jwtTokenKey = UserCacheConstant.JWT_TOKEN + userToken;
//        //key：uuid   value:jwttoken
//        redisTemplate.opsForValue().set(jwtTokenKey, jwtToken, ttl, TimeUnit.SECONDS);
//
//        return userVoResult;
//    }
}
