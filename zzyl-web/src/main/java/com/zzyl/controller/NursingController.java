package com.zzyl.controller;

import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.dto.NursingPlanDto;
import com.zzyl.service.NursingPlanService;
import com.zzyl.vo.NursingPlanVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nursing")
@Api(tags = "护理计划相关接口")
public class NursingController {

    @Autowired
    private NursingPlanService nursingPlanService;



    @ApiOperation("添加护理计划")
    @PostMapping("/plan")
    public ResponseResult addNursingPlan(
            @ApiParam(value = "护理计划数据", required = true)
            @RequestBody NursingPlanDto nursingPlan) {
        nursingPlanService.add(nursingPlan);
        return ResponseResult.success();
    }

    @ApiOperation("修改护理计划")
    @PutMapping("/plan/{id}")
    public ResponseResult updateNursingPlan(
            @ApiParam(value = "护理计划ID", required = true)
            @PathVariable Long id,
            @ApiParam(value = "护理计划数据", required = true)
            @RequestBody NursingPlanDto nursingPlan) {
        nursingPlanService.update(nursingPlan);
        return ResponseResult.success();
    }

    @ApiOperation("删除护理计划")
    @DeleteMapping("/plan/{id}")
    public ResponseResult deleteNursingPlan(
            @ApiParam(value = "护理计划ID", required = true)
            @PathVariable Long id) {
        nursingPlanService.deleteById(id);
        return ResponseResult.success();
    }

    @ApiOperation("根据ID查询护理计划")
    @GetMapping("/plan/{id}")
    public ResponseResult<NursingPlanVo> getNursingPlanById(
            @ApiParam(value = "护理计划ID", required = true)
            @PathVariable Long id) {
        return ResponseResult.success(nursingPlanService.getById(id));
    }

    @ApiOperation("查询所有护理计划")
    @GetMapping("/plan")
    public ResponseResult<List<NursingPlanVo>> getAllNursingPlan() {
        return ResponseResult.success(nursingPlanService.listAll());
    }

    @ApiOperation("根据名称和状态分页查询")
    @GetMapping("/plan/search")
    public ResponseResult<PageResponse<NursingPlanVo>> searchNursingPlan(
            @ApiParam(value = "护理计划名称")
            @RequestParam(required = false) String name,
            @ApiParam(value = "护理计划状态")
            @RequestParam(required = false) Integer status,
            @ApiParam(value = "页码（默认为1）")
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页大小（默认为10）")
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return ResponseResult.success(nursingPlanService.listByPage(name, status, pageNum, pageSize));
    }

    @PutMapping("/{id}/status/{status}")
    @ApiOperation("启用/禁用护理计划")
    public ResponseResult enableOrDisable(
            @ApiParam(value = "护理计划ID", required = true)
            @PathVariable Long id,
            @ApiParam(value = "状态，0：禁用，1：启用", required = true)
            @PathVariable Integer status) {
        nursingPlanService.enableOrDisable(id, status);
        return ResponseResult.success();
    }
}
