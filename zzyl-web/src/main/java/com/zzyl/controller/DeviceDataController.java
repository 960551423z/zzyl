package com.zzyl.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.dto.DeviceDataDto;
import com.zzyl.service.DeviceDataService;
import com.zzyl.utils.ObjectUtil;
import com.zzyl.vo.DeviceDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/device-data")
@Api(tags = "设备数据管理接口")
public class DeviceDataController {

    @Resource
    private DeviceDataService deviceDataService;

    @PostMapping("/create")
    @ApiOperation(value = "创建设备数据", notes = "接收一个DeviceDataDto对象作为请求参数，返回void")
    public ResponseResult createDeviceData(@RequestBody @ApiParam(value = "设备数据", required = true) DeviceDataDto deviceDataDto) {
        deviceDataService.createDeviceData(deviceDataDto);
        return ResponseResult.success();
    }

    @GetMapping("/read/{id}")
    @ApiOperation(value = "读取设备数据", notes = "接收一个id参数作为请求参数，返回一个DeviceDataVo对象")
    public ResponseResult<DeviceDataVo> readDeviceData(@PathVariable @ApiParam(value = "设备数据ID", required = true) Long id) {
        return ResponseResult.success(deviceDataService.readDeviceData(id));
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "处理", notes = "接收一个id参数和一个DeviceDataDto对象作为请求参数，返回void")
    public ResponseResult updateDeviceData(@PathVariable @ApiParam(value = "设备数据ID", required = true) Long id, @RequestBody @ApiParam(value = "设备数据", required = true) DeviceDataDto deviceDataDto) {
        deviceDataService.updateDeviceData(id, deviceDataDto);
        return ResponseResult.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除设备数据", notes = "接收一个id参数作为请求参数，返回void")
    public ResponseResult deleteDeviceData(@PathVariable @ApiParam(value = "设备数据ID", required = true) Long id) {
        deviceDataService.deleteDeviceData(id);
        return ResponseResult.success();
    }

    @GetMapping("/get-page")
    @ApiOperation(value = "获取设备数据分页结果", notes = "接收包含分页信息的请求参数，返回一个包含分页数据的Page<DeviceDataDto>对象")
    public ResponseResult<PageResponse<DeviceDataVo>> getDeviceDataPage(
            @ApiParam(value = "页码", required = true) @RequestParam("pageNum") Integer pageNum,
            @ApiParam(value = "每页大小", required = true) @RequestParam("pageSize") Integer pageSize,
            @ApiParam(value = "设备名称") @RequestParam(value = "deviceName", required = false) String deviceName,
            @ApiParam(value = "接入位置") @RequestParam(value = "accessLocation", required = false) String accessLocation,
            @ApiParam(value = "位置类型") @RequestParam(value = "accessLocation", required = false) Integer locationType,
            @ApiParam(value = "功能ID") @RequestParam(value = "functionId", required = false) String functionId,
            @ApiParam(value = "开始时间")  @RequestParam(required = false) Long startTime,
            @ApiParam(value = "结束时间")  @RequestParam(required = false) Long endTime,
            @ApiParam(value = "状态 0 正常 1 异常 2待处理 3已处理")  @RequestParam(required = false) Integer status) {
        return ResponseResult.success(deviceDataService.getDeviceDataPage(pageNum, pageSize, status, deviceName, accessLocation, locationType, functionId, ObjectUtil.isEmpty(startTime)? null : LocalDateTimeUtil.of(startTime), ObjectUtil.isEmpty(endTime)? null : LocalDateTimeUtil.of(endTime)));
    }
}