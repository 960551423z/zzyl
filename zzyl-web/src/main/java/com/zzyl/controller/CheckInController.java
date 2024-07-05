package com.zzyl.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONUtil;
import com.zzyl.base.ResponseResult;
import com.zzyl.dto.CheckInConfigDto;
import com.zzyl.dto.CheckInDto;
import com.zzyl.dto.ContractDto;
import com.zzyl.service.CheckInConfigService;
import com.zzyl.service.CheckInService;
import com.zzyl.service.ContractService;
import com.zzyl.utils.UserThreadLocal;
import com.zzyl.vo.CheckInVo;
import com.zzyl.vo.UserVo;
import com.zzyl.vo.retreat.TasVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/checkIn")
@Api(tags = "入住")
public class CheckInController {

    @Autowired
    private CheckInService checkInService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private CheckInConfigService checkInConfigService;


    @PostMapping("/create")
    @ApiOperation(value = "申请入住", notes = "传入入住对象")
    public ResponseResult<CheckInVo> createCheckIn(
            @RequestBody @ApiParam(value = "入住对象", required = true) CheckInDto checkInDto) {
        return checkInService.createCheckIn(checkInDto);
    }

    @PostMapping("/review")
    @ApiOperation(value = "评估", notes = "传入入住对象")
    public ResponseResult<CheckInVo> review(
            @RequestBody @ApiParam(value = "入住对象", required = true) CheckInDto checkInDto) {
        return checkInService.review(checkInDto);
    }

    @GetMapping
    @ApiOperation(value = "入住表单查询")
    public ResponseResult<TasVo> getCheckIn(
            @RequestParam @ApiParam(value = "入住编码") String code,
            @RequestParam @ApiParam(value = "处理人ID") String assigneeId,
            @RequestParam @ApiParam(value = "流程状态") Integer flowStatus,
            @RequestParam(required = false) @ApiParam(value = "任务Id") String taskId) {
        return checkInService.getCheckIn(code, assigneeId, flowStatus, taskId);
    }

    @PutMapping("/submit")
    @ApiOperation(value = "同意")
    public ResponseResult submitCheckIn(
            @RequestParam @ApiParam(value = "入住Id") Long id,
            @RequestParam @ApiParam(value = "审批意见") String message,
            @RequestParam @ApiParam(value = "任务Id") String taskId) {
        return checkInService.submitCheckIn(id, message, taskId);
    }

    @PutMapping
    @ApiOperation(value = "驳回")
    public ResponseResult disapprove(
            @RequestParam @ApiParam(value = "入住Id") Long id,
            @RequestParam @ApiParam(value = "驳回消息") String message,
            @RequestParam @ApiParam(value = "任务Id") String taskId) {
        return checkInService.disapprove(id, message, taskId);
    }

    @PutMapping("/reject")
    @ApiOperation(value = "审核拒绝")
    public ResponseResult auditReject(
            @RequestParam @ApiParam(value = "入住Id") Long id,
            @RequestParam @ApiParam(value = "拒绝原因") String message,
            @RequestParam @ApiParam(value = "任务Id") String taskId) {
        return checkInService.auditReject(id, message, taskId);
    }

    @PutMapping("/revocation")
    @ApiOperation(value = "撤回")
    public ResponseResult revocation(
            @RequestParam @ApiParam(value = "入住Id") Long id,
            @RequestParam @ApiParam(value = "流程状态") Integer flowStatus,
            @RequestParam @ApiParam(value = "任务Id") String taskId) {
        return checkInService.revocation(id, flowStatus, taskId);
    }

    @PutMapping("/cancel")
    @ApiOperation(value = "撤销")
    public ResponseResult cancel(@RequestParam @ApiParam(value = "入住Id") Long id,
                                 @RequestParam @ApiParam(value = "任务ID") String taskId){
        return checkInService.cancel(id, taskId);
    }

    @ApiOperation(value = "入住管理", notes = "入住管理列表页")
    @GetMapping("/selectByPage")
    public ResponseResult selectByPage(
            @ApiParam(value = "入住编码") @RequestParam(required = false) String checkInCode,
            @ApiParam(value = "姓名") @RequestParam(required = false) String name,
            @ApiParam(value = "身份证号") @RequestParam(required = false) String idCardNo,
            @ApiParam(value = "开始时间") @RequestParam(required = false) Long startTime,
            @ApiParam(value = "结束时间") @RequestParam(required = false) Long endTime,
            @ApiParam(value = "页码") @RequestParam(required = true,defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(required = true,defaultValue = "10") Integer pageSize
    ) {
        String subject = UserThreadLocal.getSubject();
        UserVo userVo = JSONUtil.toBean(subject, UserVo.class);
        String deptNo = userVo.getDeptNo();
        Long userId = userVo.getId();

        LocalDateTime start = null;
        LocalDateTime end = null;
        if(startTime != null && endTime != null){
            start = LocalDateTimeUtil.of(startTime);
            end = LocalDateTimeUtil.of(endTime);
        }

        return checkInService.selectByPage(checkInCode, name, idCardNo, start, end, pageNum, pageSize,deptNo, userId);
    }

    /**
     * 入住配置
     * @param checkInConfigDto 入住配置
     * @return 操作结果
     */
    @PostMapping
    @ApiOperation(value = "入住配置")
    public ResponseResult checkIn(@RequestBody CheckInConfigDto checkInConfigDto) {
        checkInConfigService.checkIn(checkInConfigDto);
        return ResponseResult.success();
    }

    /**
     * 签约办理
     * @param contractDto 合同信息
     * @return 操作结果
     */
    @PostMapping("/sign")
    @ApiOperation(value = "签约办理")
    public ResponseResult sign(@RequestBody ContractDto contractDto) {
        contractService.sign(contractDto);
        return ResponseResult.success();
    }
}
