package com.zzyl.controller;

import com.zzyl.base.ResponseResult;
import com.zzyl.dto.RetreatDto;
import com.zzyl.dto.RetreatReqDto;
import com.zzyl.entity.Retreat;
import com.zzyl.service.RetreatService;
import com.zzyl.service.UserService;
import com.zzyl.vo.retreat.TasVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/elder")
@Api(tags = "退住")
public class RetreatController {

    @Autowired
    private RetreatService retreatService;

    @PostMapping("/create")
    @ApiOperation(value = "申请退住", notes = "传入退住对象")
    public ResponseResult createRetreat(
            @RequestBody @ApiParam(value = "退住对象", required = true) Retreat retreatDto) {
        return retreatService.createRetreat(retreatDto);
    }

    @GetMapping
    @ApiOperation(value = "退住表单查询")
    public ResponseResult<TasVo> getRetreat(
            @RequestParam @ApiParam(value = "退住编码") String retreatCode,
            @RequestParam(required = false) @ApiParam(value = "处理人ID") String assigneeId,
            @RequestParam @ApiParam(value = "流程状态") Integer flowStatus,
            @RequestParam(required = false) @ApiParam(value = "任务id")String taskId) {
        return retreatService.getRetreat(retreatCode, assigneeId, flowStatus, taskId);
    }

    @PostMapping("/submit")
    @ApiOperation(value = "提交")
    public ResponseResult submitRetreat(
            @RequestBody @ApiParam(value = "退住对象", required = true) RetreatDto retreatDto) {
        return retreatService.submitRetreat(retreatDto);
    }

    @PutMapping
    @ApiOperation(value = "驳回")
    public ResponseResult disapprove(
            @RequestParam @ApiParam(value = "退住编码") String retreatCode,
            @RequestParam @ApiParam(value = "驳回消息") String message,
            @RequestParam @ApiParam(value = "任务Id") String taskId) {
        return retreatService.disapprove(retreatCode, message, taskId);
    }

    @PutMapping("/reject")
    @ApiOperation(value = "审核拒绝")
    public ResponseResult auditReject(
            @RequestParam @ApiParam(value = "退退住编码") String retreatCode,
            @RequestParam @ApiParam(value = "拒绝原因") String reject,
            @RequestParam @ApiParam(value = "任务Id") String taskId) {
        return retreatService.auditReject(retreatCode, reject, taskId);
    }

    @PutMapping("/revocation")
    @ApiOperation(value = "撤回")
    public ResponseResult revocation(
            @RequestParam @ApiParam(value = "退住编码") String retreatCode,
            @RequestParam @ApiParam(value = "流程状态") Integer flowStatus,
            @RequestParam @ApiParam(value = "任务Id") String taskId) {
        return retreatService.revocation(retreatCode, flowStatus, taskId);
    }

    @PutMapping("/cancel")
    @ApiOperation(value = "撤销")
    public ResponseResult cancel(@RequestParam @ApiParam(value = "退住编码") String retreatCode,
                                 @RequestParam @ApiParam(value = "任务Id") String taskId){
        return retreatService.cancel(retreatCode, taskId);
    }

    @Autowired
    private UserService userService;


    @ApiOperation(value = "退住管理", notes = "退住管理列表页")
    @PostMapping("/selectByPage")
    public ResponseResult selectByPage(@RequestBody RetreatReqDto retreatReqDto) {
        return retreatService.selectByPage(retreatReqDto);
    }
}
