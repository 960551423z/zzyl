package com.zzyl.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zzyl.base.ResponseResult;
import com.zzyl.dto.DeptDto;
import com.zzyl.service.DeptService;
import com.zzyl.vo.DeptVo;
import com.zzyl.vo.TreeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: 阿庆
 * @date: 2024/7/8 下午2:49
 */

@RestController
@RequestMapping("/dept")
@Api(tags = "部门管理")
public class DeptController {

    @Autowired
    private DeptService deptService;


    @PostMapping("/list")
    @ApiOperation("部门列表")
    @ApiOperationSupport(includeParameters = {
            "deptDto.parentDeptNo", "deptDto.deptName","deptDto.dataState"
    })
    public ResponseResult<List<DeptVo>> list(@RequestBody DeptDto deptDto) {
        List<DeptVo> deptList = deptService.findDeptList(deptDto);
        return ResponseResult.success(deptList);
    }


    @PostMapping("/tree")
    @ApiOperation("部门树形化")
    public ResponseResult<TreeVo> deptTree(@RequestBody DeptDto deptDto) {
        TreeVo treeVo = deptService.deptTree();
        return ResponseResult.success(treeVo);
    }

    @PutMapping
    @ApiOperation(value = "部门添加",notes = "部门添加")
    @ApiImplicitParam(name = "deptDto",value = "部门DTO对象",required = true,dataType = "DeptDto")
    @ApiOperationSupport(includeParameters = {
            "deptDto.dataState",
            "deptDto.deptName",
            "deptDto.leaderId",
            "deptDto.remark",
            "deptDto.sortNo",
            "deptDto.parentDeptNo"})
    public ResponseResult<Boolean> createDept(@RequestBody DeptDto deptDto) {
        boolean flag = deptService.createDept(deptDto);
        return ResponseResult.success(flag);
    }

    @PatchMapping
    @ApiOperation("部门修改")
    @ApiImplicitParam(name = "deptDto",value = "部门DTO对象",required = true,dataType = "DeptDto")
    @ApiOperationSupport(includeParameters = {
            "deptDto.dataState",
            "deptDto.deptName",
            "deptDto.leaderId",
            "deptDto.remark",
            "deptDto.sortNo",
            "deptDto.parentDeptNo"})
    public ResponseResult<Boolean> updateDept(@RequestBody DeptDto deptDto) {
        boolean flag = deptService.updateDept(deptDto);
        return ResponseResult.success(flag);
    }

    @PatchMapping("/is_enable")
    @ApiOperation("部门启用禁用")
    @ApiImplicitParam(name = "deptDto",value = "部门DTO对象",required = true,dataType = "DeptDto")
    @ApiOperationSupport(includeParameters = {
            "deptDto.dataState",
            "deptDto.id"
    })
    public ResponseResult<Boolean> isEnable(@RequestBody DeptDto deptDto) {
        boolean flag = deptService.isEnable(deptDto);
        return ResponseResult.success(flag);
    }


    @DeleteMapping("/{deptId}")
    @ApiOperation("部门删除")
    public ResponseResult<Integer> deleteDept(@PathVariable Long deptId) {
        int flag = deptService.removeById(deptId);
        return ResponseResult.success(flag);
    }

}
