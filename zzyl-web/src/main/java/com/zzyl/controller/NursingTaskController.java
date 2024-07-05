package com.zzyl.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.entity.NursingTask;
import com.zzyl.service.NursingTaskService;
import com.zzyl.utils.ObjectUtil;
import com.zzyl.vo.NursingTaskVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/nursingTask")
@Api(tags = "护理任务相关接口")
public class NursingTaskController {

    @Resource
    private NursingTaskService nursingTaskService;

    /**
     * 根据ID获取护理任务
     * @param taskId 任务ID
     * @return 护理任务
     */
    @GetMapping
    @ApiOperation("根据ID获取护理任务")
    @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "Long", paramType = "query")
    ResponseResult<NursingTask> getById(@RequestParam("taskId") Long taskId) {
        return ResponseResult.success(nursingTaskService.selectByPrimaryKey(taskId));
    }

    /**
     * 取消任务
     * @param taskId 任务ID
     * @param reason 取消原因
     * @return 取消结果
     */
    @PutMapping("cancel")
    @ApiOperation("取消任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "reason", value = "取消原因", required = true, dataType = "String", paramType = "query")
    })
    ResponseResult cancelTask(@RequestParam("taskId") Long taskId, @RequestParam("reason") String reason) {
        nursingTaskService.cancelTask(taskId, reason);
        return ResponseResult.success();
    }

    /**
     * 任务改期
     * @param taskId 任务ID
     * @param estimatedServerTime 新的执行时间
     * @return 改期结果
     */
    @PutMapping("updateTime")
    @ApiOperation("任务改期")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "estimatedServerTime", value = "新的执行时间", required = true, dataType = "Long", paramType = "query")
    })
    ResponseResult rescheduleTask(@RequestParam("taskId") Long taskId, @RequestParam("estimatedServerTime") Long estimatedServerTime) {
              nursingTaskService.rescheduleTask(taskId, LocalDateTimeUtil.of(estimatedServerTime));
        return ResponseResult.success();
    }

    /**
     * 任务执行
     * @param taskId 任务ID
     * @param estimatedServerTime 预计执行时间
     * @param taskImage 任务图片
     * @param mark 备注
     * @return 执行结果
     */
    @PutMapping("do")
    @ApiOperation("任务执行")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "estimatedServerTime", value = "预计执行时间", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "taskImage", value = "任务图片", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mark", value = "备注", required = true, dataType = "String", paramType = "query")
    })
    ResponseResult executeTask(@RequestParam("taskId") Long taskId, @RequestParam("estimatedServerTime") Long estimatedServerTime, @RequestParam("taskImage") String taskImage, @RequestParam("mark") String mark) {
        nursingTaskService.executeTask(taskId, LocalDateTimeUtil.of(estimatedServerTime), taskImage, mark);
        return ResponseResult.success();
    }

    @GetMapping("page")
    @ApiOperation("分页查询任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页大小", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "elderName", value = "老人姓名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "nurseId", value = "护理员ID", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "projectId", value = "项目ID", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态  1待执行 2已执行 3已关闭 ", dataType = "Integer", paramType = "query")
    })
    ResponseResult<PageResponse<NursingTask>> getTasksByPage(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, @Param("elderName") String elderName, @Param("nurseId") Long nurseId, @Param("projectId") Long projectId, @Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("status") Integer status) {
        PageResponse<NursingTaskVo> tasksByPage = nursingTaskService.getTasksByPage(pageNum, pageSize, elderName, nurseId, projectId, ObjectUtil.isEmpty(startTime)? null : LocalDateTimeUtil.of(startTime), ObjectUtil.isEmpty(endTime)? null : LocalDateTimeUtil.of(endTime), status);
        return ResponseResult.success(tasksByPage);
    }




}
