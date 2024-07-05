package com.zzyl.controller;

import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.dto.AlertRuleDto;
import com.zzyl.service.AlertRuleService;
import com.zzyl.vo.AlertRuleVo;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/alert-rule")
@Api(tags = "告警规则管理接口")
public class AlertRuleController {

    @Resource
    private AlertRuleService alertRuleService;

    @PostMapping("/create")
    @ApiOperation(value = "创建告警规则", notes = "接收一个AlertRuleDto对象作为请求参数，返回void")
    public ResponseResult createAlertRule(@RequestBody AlertRuleDto alertRuleDto) {
        alertRuleService.createAlertRule(alertRuleDto);
        return ResponseResult.success();
    }

    @GetMapping("/read/{id}")
    @ApiOperation(value = "获取单个告警规则", notes = "接收一个id参数作为请求参数，返回一个AlertRuleDto对象")
    public ResponseResult<AlertRuleVo> readAlertRule(@PathVariable Long id) {
        return ResponseResult.success(alertRuleService.readAlertRule(id));
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "更新告警规则", notes = "接收一个id参数和一个AlertRuleDto对象作为请求参数，返回void")
    public ResponseResult updateAlertRule(@PathVariable Long id, @RequestBody AlertRuleDto alertRuleDto) {
        alertRuleService.updateAlertRule(id, alertRuleDto);
        return ResponseResult.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除告警规则", notes = "接收一个id参数作为请求参数，返回void")
    public ResponseResult deleteAlertRule(@PathVariable Long id) {
        alertRuleService.deleteAlertRule(id);
        return ResponseResult.success();
    }

    @GetMapping("/get-page")
    @ApiOperation(value = "分页获取告警规则列表", notes = "接收一个Pageable参数作为请求参数，返回一个包含分页数据的Page<AlertRuleDto>")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功获取分页数据"),
            @ApiResponse(code = 500, message = "内部服务器错误")
    })
    public ResponseResult<PageResponse<AlertRuleVo>> getAlertRulePage(@ApiParam(value = "页码", required = true) @RequestParam("pageNum") Integer pageNum,
                                                                      @ApiParam(value = "每页大小", required = true) @RequestParam("pageSize") Integer pageSize,
                                                                      @ApiParam(value = "规则名称") @RequestParam(value = "alertRuleName" ,required = false) String alertRuleName,
                                                                      @ApiParam(value = "产品ID") @RequestParam(value = "productKey" , required = false) String productKey,
                                                                      @ApiParam(value = "功能名称") @RequestParam(value = "functionName", required = false) String functionName) {
        return ResponseResult.success(alertRuleService.getAlertRulePage(pageNum, pageSize, alertRuleName, productKey, functionName));
    }


    @PutMapping("/{id}/status/{status}")
    @ApiOperation("启用/禁用")
    public ResponseResult enableOrDisable(@PathVariable Long id, @PathVariable Integer status) {
        alertRuleService.enableOrDisable(id, status);
        return ResponseResult.success();
    }
}