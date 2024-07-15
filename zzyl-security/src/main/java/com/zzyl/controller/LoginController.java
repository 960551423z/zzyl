package com.zzyl.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zzyl.base.ResponseResult;
import com.zzyl.dto.LoginDto;
import com.zzyl.service.LoginService;
import com.zzyl.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *  登录接口
 */
@RestController
@Api(tags = "用户登录")
@RequestMapping("/security")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    @ApiOperation("登录")
    public ResponseResult<UserVo> login(@RequestBody LoginDto loginDto) {
        UserVo userVo = loginService.login(loginDto);
        return ResponseResult.success(userVo);
    }

//    @PostMapping("/login")
//    @ApiOperation(value = "用户登录",notes = "用户登录")
//    @ApiImplicitParam(name = "userVo",value = "登录对象",required = true,dataType = "UserVo")
//    @ApiOperationSupport(includeParameters ={"userVo.username","userVo.password"} )
//    public ResponseResult<UserVo> login(@RequestBody UserVo userVo){
//        return ResponseResult.success(loginService.login(userVo));
//    }
}
