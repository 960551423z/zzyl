package com.zzyl.intercept;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.zzyl.constant.Constants;
import com.zzyl.constant.SecurityConstant;
import com.zzyl.exception.BaseException;
import com.zzyl.properties.JwtTokenManagerProperties;
import com.zzyl.utils.JwtUtil;
import com.zzyl.utils.UserThreadLocal;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

import static com.zzyl.constant.Constants.JWT_AUTHORITIES;
import static com.zzyl.constant.Constants.JWT_USERID;
import static com.zzyl.constant.SecurityConstant.USER_TOKEN;

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


    @Autowired
    private JwtTokenManagerProperties jwtTokenManagerProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 判断是否是handler（controller 中方法）
        if (!(handler instanceof HandlerMethod))
            return true;

        String token = request.getHeader(USER_TOKEN);
        if (ObjectUtil.isEmpty(token)) {
            // token 失效
            throw new BaseException("小程序登录","401",null,"没有权限,请登录");
        }

        // 解析
        Claims claims = JwtUtil.parseJWT(jwtTokenManagerProperties.getBase64EncodedSecretKey(), token);
        if (ObjectUtil.isEmpty(claims))
            throw new BaseException("小程序登录","401",null,"没有权限,请登录");

        // 获取数据
        Long userId = MapUtil.get(claims, JWT_USERID, Long.class);
        if (ObjectUtil.isEmpty(userId))
            throw new BaseException("小程序登录","401",null,"没有权限,请登录");


        // 存储到ThreadLocal中
        UserThreadLocal.set(userId);

        return true;


    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }

}
