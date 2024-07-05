package com.zzyl.service.impl;

import com.zzyl.service.UserService;
import com.zzyl.vo.UserAuth;
import com.zzyl.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 *  用户明细信息服务
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //这里我们默认使用账号密码登录,对于多种登录方式如何处理-->字符串分割
        UserVo userVo = userService.findUserVoForLogin(username);
        return new UserAuth(userVo);
    }
}
