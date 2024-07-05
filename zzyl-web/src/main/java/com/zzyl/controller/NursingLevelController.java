package com.zzyl.controller;

import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.dto.NursingLevelDto;
import com.zzyl.service.NursingLevelService;
import com.zzyl.vo.NursingLevelVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nursingLevel")
@Api(tags = "护理等级管理")
public class NursingLevelController {
    @Autowired
    private NursingLevelService nursingLevelService;

    @GetMapping("/listAll")
    @ApiOperation(value = "查询所有护理等级信息")
    public ResponseResult<List<NursingLevelVo>> listAll() {
        List<NursingLevelVo> nursingLevelDtos = nursingLevelService.listAll();
        return ResponseResult.success(nursingLevelDtos);
    }

    @PostMapping("/insertBatch")
    @ApiOperation(value = "批量插入护理等级信息")
    public ResponseResult insertBatch(
            @ApiParam(value = "护理等级数据列表", required = true)
            @RequestBody List<NursingLevelDto> nursingLevelDtos) {
        nursingLevelService.insertBatch(nursingLevelDtos);
        return ResponseResult.success();
    }

    @PostMapping("/insert")
    @ApiOperation(value = "插入护理等级信息")
    public ResponseResult insert(
            @ApiParam(value = "护理等级数据", required = true)
            @RequestBody NursingLevelDto nursingLevel) {
        nursingLevelService.insert(nursingLevel);
        return ResponseResult.success();
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新护理等级信息")
    public ResponseResult update(
            @ApiParam(value = "护理等级数据", required = true)
            @RequestBody NursingLevelDto nursingLevel) {
        nursingLevelService.update(nursingLevel);
        return ResponseResult.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除护理等级信息")
    public ResponseResult delete(
            @ApiParam(value = "护理等级ID", required = true)
            @PathVariable("id") Long id) {
        nursingLevelService.delete(id);
        return ResponseResult.success();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询护理等级信息")
    public ResponseResult<NursingLevelVo> findById(
            @ApiParam(value = "护理等级ID", required = true)
            @PathVariable("id") Long id) {
        NursingLevelVo nursingLevelVo = nursingLevelService.getById(id);
        return ResponseResult.success(nursingLevelVo);
    }

    @GetMapping("/listByPage")
    @ApiOperation(value = "分页查询护理等级信息")
    public ResponseResult<PageResponse<NursingLevelVo>> listByPage(
            @ApiParam(value = "页码", required = true)
            @RequestParam("pageNum") Integer pageNum,
            @ApiParam(value = "每页大小", required = true)
            @RequestParam("pageSize") Integer pageSize,
            @ApiParam(value = "护理等级名称")
            @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "护理等级状态")
            @RequestParam(value = "status", required = false) Integer status) {
        PageResponse<NursingLevelVo> nursingLevelVoPageResponse = nursingLevelService.listByPage(pageNum, pageSize, name, status);
        return ResponseResult.success(nursingLevelVoPageResponse);
    }

    @PutMapping("/{id}/status/{status}")
    @ApiOperation("启用/禁用护理等级")
    public ResponseResult enableOrDisable(
            @ApiParam(value = "护理等级ID", required = true)
            @PathVariable Long id,
            @ApiParam(value = "状态，0：禁用，1：启用", required = true)
            @PathVariable Integer status) {
        nursingLevelService.enableOrDisable(id, status);
        return ResponseResult.success();
    }
}
