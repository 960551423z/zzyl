package com.zzyl.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.dto.UserDto;
import com.zzyl.service.UserService;
import com.zzyl.utils.UserThreadLocal;
import com.zzyl.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户前端控制器
 */
@Slf4j
@Api(tags = "用户管理")
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    /***
     *  多条件查询用户分页列表
     * @param userDto 用户DTO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<UserVo>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "用户分页",notes = "用户分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userDto",value = "用户DTO对象",required = true,dataType = "UserDto"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"userDto.username","userDto.email","userDto.dataState","userDto.deptNo"})
    public ResponseResult<PageResponse<UserVo>> findUserVoPage(
                                    @RequestBody UserDto userDto,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        PageResponse<UserVo> userVoPage = userService.findUserPage(userDto, pageNum, pageSize);
        return ResponseResult.success(userVoPage);
    }

    /**
     *  保存用户
     * @param userDto 用户Vo对象
     * @return UserDto
     */
    @PutMapping
    @ApiOperation(value = "用户添加",notes = "用户添加")
    @ApiImplicitParam(name = "userDto",value = "用户DTO对象",required = true,dataType = "UserDto")
    @ApiOperationSupport(includeParameters = {"userDto.email","userDto.dataState","userDto.deptNo","userDto.deptPostUserVoSet","userDto.mobile","userDto.postNo","userDto.realName","userDto.roleVoIds"})
    public ResponseResult<UserVo> createUser(@RequestBody UserDto userDto) {
        UserVo userVoResult = userService.createUser(userDto);
        return ResponseResult.success(userVoResult);
    }
    /**
     *  修改用户
     * @param userId 用户Vo对象
     * @return Boolean 是否修改成功
     */
    @PostMapping("reset-passwords/{userId}")
    @ApiOperation(value = "密码重置",notes = "密码重置")
    @ApiImplicitParam(paramType = "path",name = "userId",value = "用戶Id",required = true,dataType = "String")
    public ResponseResult<Boolean> resetPasswords(@PathVariable("userId") String userId) {
        Boolean flag = userService.resetPasswords(userId);
        return ResponseResult.success(flag);
    }


    /**
     *  修改用户
     * @param userDto 用户DTO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "用户修改",notes = "用户修改")
    @ApiImplicitParam(name = "userDto",value = "用户DTO对象",required = true,dataType = "UserDto")
    @ApiOperationSupport(includeParameters = {"userDto.email","userDto.dataState","userDto.deptNo","userDto.deptPostUserVoSet","userDto.mobile","userDto.postNo","userDto.realName","userDto.roleVoIds"})
    public ResponseResult<Boolean> updateUser(@RequestBody UserDto userDto) {
        Boolean flag = userService.updateUser(userDto);
        return ResponseResult.success(flag);
    }

    /***
     *  多条件查询用户列表
     * @param userVo 用户Vo对象
     * @return List<UserVo>
     */
    @PostMapping("list")
    @ApiOperation(value = "用户列表",notes = "用户列表")
    @ApiImplicitParam(name = "userVo",value = "用户Vo对象",required = true,dataType = "UserVo")
    public ResponseResult<List<UserVo>> userList(@RequestBody UserVo userVo) {
        List<UserVo> userVoList = userService.findUserList(userVo);
        return ResponseResult.success(userVoList);
    }

    @GetMapping("current-user")
    @ApiOperation(value = "当前用户",notes = "当前用户")
    ResponseResult<UserVo> findCurrentUser()  {
        String subject = UserThreadLocal.getSubject();
        UserVo userVo = JSONObject.parseObject(subject,UserVo.class);
        return ResponseResult.success(userVo);
    }


    @ApiOperation("删除用户")
    @DeleteMapping("/remove/{userIds}")
    public ResponseResult remove(@PathVariable List<Long> userIds) {
        return ResponseResult.success(userService.deleteUserByIds(userIds));
    }
}
