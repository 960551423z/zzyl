package com.zzyl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zzyl.constant.UserCacheConstant;
import com.zzyl.service.LogoutService;
import com.zzyl.utils.UserThreadLocal;
import com.zzyl.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 *  退出接口实现
 */
@Component
public class LogoutServiceImpl implements LogoutService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Boolean logout() {
        String subject = UserThreadLocal.getSubject();
        UserVo userVo = JSONObject.parseObject(subject,UserVo.class);
        //移除缓存：用户关联userToken
        String userTokenKey = UserCacheConstant.USER_TOKEN+userVo.getUsername();
        Boolean isDelete = redisTemplate.delete(userTokenKey);
        if (!isDelete){
            throw new RuntimeException("退出失败！");
        }
        //语出缓存：userToken关联jwtToken
        String jwtTokenKey = UserCacheConstant.JWT_TOKEN+userVo.getUserToken();
        isDelete = redisTemplate.delete(jwtTokenKey);
        if (!isDelete){
            throw new RuntimeException("退出失败！");
        }
        return isDelete;
    }
}
