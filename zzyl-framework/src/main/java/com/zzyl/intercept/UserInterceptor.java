package com.zzyl.intercept;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.zzyl.constant.Constants;
import com.zzyl.exception.BaseException;
import com.zzyl.properties.JwtTokenManagerProperties;
import com.zzyl.utils.JwtUtil;
import com.zzyl.utils.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 小程序端token校验
 * 统一对请求的合法性进行校验，需要进行2个方面的校验，
 * 一、请求头中是否携带了authorization
 * 二、请求头中是否存在userId，如果不存在则说明是非法请求，响应401状态码
 * 如果是合法请求，将userId存储到ThreadLocal中
 */
@Slf4j
@Component
@EnableConfigurationProperties(JwtTokenManagerProperties.class)
public class UserInterceptor implements HandlerInterceptor {

    @Value("${token.header:authorization}")
    private String header;

    @Autowired
    private JwtTokenManagerProperties jwtTokenManagerProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //如果不是映射到方法就放行，比如跨域验证请求、静态资源等不需要身份校验的请求
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        //获取header的参数
        String token = request.getHeader(header);

        log.info("开始解析 customer user token {}", token);
        if (ObjectUtil.isEmpty(token)) {
            //token失效
            throw new BaseException("小程序登录","401",null,"没有权限,请登录");
        }
        Map<String, Object> claims = JwtUtil.parseJWT(jwtTokenManagerProperties.getBase64EncodedSecretKey(), token);
        if (ObjectUtil.isEmpty(claims)) {
            //token失效
            throw new BaseException("小程序登录","401",null,"没有权限,请登录");
        }

        Long userId = MapUtil.get(claims, Constants.JWT_USERID, Long.class);
        if (ObjectUtil.isEmpty(userId)) {
            throw new BaseException("小程序登录","401",null,"没有权限,请登录");
        }

        UserThreadLocal.set(userId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
