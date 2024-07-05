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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门前端控制器
 */
@Slf4j
@Api(tags = "部门管理")
@RestController
@RequestMapping("dept")
public class DeptController {

    @Autowired
    DeptService deptService;


    /**
     *  保存部门
     * @param deptDto 部门DTO对象
     * @return Boolean
     */
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
    public ResponseResult<DeptVo> createDept(@RequestBody DeptDto deptDto) {
        return ResponseResult.success(deptService.createDept(deptDto));
    }

    /**
     *  修改部门
     * @param deptDto 部门DTO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "部门修改",notes = "部门修改")
    @ApiImplicitParam(name = "deptDto",value = "部门DTO对象",required = true,dataType = "DeptDto")
    public ResponseResult<Boolean> updateDept(@RequestBody DeptDto deptDto) {
        return ResponseResult.success(deptService.updateDept(deptDto));
    }

    @PatchMapping("/is_enable")
    @ApiOperation(value = "启用-禁用",notes = "启用-禁用")
    @ApiImplicitParam(name = "deptDto",value = "部门DTO对象",required = true,dataType = "DeptDto")
    @ApiOperationSupport(includeParameters = {"deptDto.dataState","deptDto.id"})
    public ResponseResult<Boolean> isEnable(@RequestBody DeptDto deptDto){
        return ResponseResult.success(deptService.isEnable(deptDto));
    }

    /**
     * 删除部门
     */
    @ApiOperation("删除部门")
    @DeleteMapping("/{deptId}")
    public ResponseResult remove(@PathVariable String deptId) {
        return ResponseResult.success(deptService.deleteDeptById(deptId));
    }

    /***
     *  多条件查询部门列表
     * @param deptDto 部门DTO对象
     * @return List<DeptVo>
     */
    @PostMapping("list")
    @ApiOperation(value = "部门列表",notes = "部门列表")
    @ApiImplicitParam(name = "deptDto",value = "部门DTO对象",required = true,dataType = "DeptDto")
    @ApiOperationSupport(includeParameters = {"deptDto.dataState","deptDto.deptName","deptDto.parentDeptNo"})
    public ResponseResult<List<DeptVo>> deptList(@RequestBody DeptDto deptDto) {
        List<DeptVo> deptVoList = deptService.findDeptList(deptDto);
        return ResponseResult.success(deptVoList);
    }

    /**
     *  组织部门树形
     * @return
     */
    @PostMapping("tree")
    @ApiOperation(value = "部门树形",notes = "部门树形")
    public ResponseResult<TreeVo> deptTreeVo() {
        TreeVo treeVo = deptService.deptTreeVo();
        return ResponseResult.success(treeVo);
    }

}
