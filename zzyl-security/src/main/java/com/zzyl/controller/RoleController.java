package com.zzyl.controller;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.dto.RoleDto;
import com.zzyl.service.RoleService;
import com.zzyl.vo.RoleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色前端控制器
 */
@Slf4j
@Api(tags = "角色管理")
@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    RoleService roleService;

    /***
     *  多条件查询角色分页列表
     * @param roleDto 角色DTO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<RoleVo>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "角色分页",notes = "角色分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "roleDto",value = "角色DTO对象",required = true,dataType = "roleDto"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"roleDto.roleName"})
    public ResponseResult<PageResponse<RoleVo>> findRoleVoPage(
                                    @RequestBody RoleDto roleDto,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        PageResponse<RoleVo> roleVoPage = roleService.findRolePage(roleDto, pageNum, pageSize);
        return ResponseResult.success(roleVoPage);
    }

    /**
     *  保存角色
     * @param roleDto 角色DTO对象
     * @return RoleVo
     */
    @PutMapping
    @ApiOperation(value = "角色添加",notes = "角色添加")
    @ApiImplicitParam(name = "roleDto",value = "角色DTO对象",required = true,dataType = "roleDto")
    @ApiOperationSupport(includeParameters = {"roleDto.roleName","roleDto.dataState"})
    public ResponseResult<RoleVo> createRole(@RequestBody RoleDto roleDto) {
        RoleVo roleVoResult = roleService.createRole(roleDto);
        return ResponseResult.success(roleVoResult);
    }

    /**
     *  修改角色
     * @param roleDto 角色Vo对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "角色修改",notes = "角色修改")
    @ApiImplicitParam(name = "roleDto",value = "角色DTO对象",required = true,dataType = "roleDto")
    @ApiOperationSupport(includeParameters = {"roleDto.roleName","roleDto.dataState","roleDto.dataScope","roleDto.checkedResourceNos","roleDto.checkedDeptNos","roleDto.id"})
    public ResponseResult<Boolean> updateRole(@RequestBody RoleDto roleDto) {
        Boolean flag = roleService.updateRole(roleDto);
        return ResponseResult.success(flag);
    }

    /**
     *  角色下拉框
     * @return
     */
    @PostMapping("init-roles")
    @ApiOperation(value = "角色下拉框",notes = "角色下拉框")
    ResponseResult<List<RoleVo>> initRoles() {
        List<RoleVo> roleVoResult = roleService.initRoles();
        return ResponseResult.success(roleVoResult);
    }

    /**
     * 删除角色
     */
    @ApiOperation("删除角色")
    @DeleteMapping("/{roleIds}")
    public ResponseResult remove(@PathVariable List<Long> roleIds) {
        return ResponseResult.success(roleService.deleteRoleByIds(roleIds));
    }
}
