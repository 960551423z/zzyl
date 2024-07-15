package com.zzyl.service;

import com.zzyl.dto.LoginDto;
import com.zzyl.vo.UserVo;

/**
 *  登录接口
 */
public interface LoginService {

    /***
     *  用户退出
     * @param userVo 登录信息
     * @return
     */
//    UserVo login(UserVo userVo);


    /**
     * 后台用户登录
     * @param loginDto 登录信息
     * @return 封装VO
     */
    UserVo login(LoginDto loginDto);
}
