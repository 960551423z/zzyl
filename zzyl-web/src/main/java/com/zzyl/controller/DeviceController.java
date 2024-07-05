package com.zzyl.controller;

import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.*;
import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.dto.DeviceDto;
import com.zzyl.service.DeviceService;
import com.zzyl.vo.DeviceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/iot")
@Api(tags = "智能监控管理相关接口")
public class DeviceController extends BaseController {

    @Resource
    Client client;

    @Value("${zzyl.aliyun.iotInstanceId}")
    private String iotInstanceId;

    @Resource
    private DeviceService deviceService;


    @PostMapping("/QueryProductList")
    @ApiOperation(value = "查看所有产品列表", notes = "查看所有产品列表")
    public ResponseResult queryProductList(@RequestBody QueryProductListRequest request) throws Exception {
        request.setIotInstanceId(iotInstanceId);
        QueryProductListResponse queryProductListResponse =
                client.queryProductList(request);
        return ResponseResult.success(queryProductListResponse.getBody().getData());
    }

    @PostMapping("/QueryDevice")
    @ApiOperation(value = "查看所有设备", notes = "查看所有产品列表")
    public ResponseResult<PageResponse<DeviceVo>> queryDevice(@RequestBody QueryDeviceRequest request) throws Exception {
        PageResponse pageResponse = deviceService.queryDevice(request);
        return ResponseResult.success(pageResponse);
    }

    @PostMapping("/QueryDeviceDetail")
    @ApiOperation(value = "查询指定设备的详细信息", notes = "查询指定设备的详细信息")
    public ResponseResult queryDeviceDetail(@RequestBody QueryDeviceDetailRequest request) throws Exception {
        DeviceVo deviceVo = deviceService.queryDeviceDetail(request);
        return ResponseResult.success(deviceVo);
    }

    @PostMapping("/QueryDevicePropertyStatus")
    @ApiOperation(value = "查询指定设备的状态", notes = "查询指定设备的状态")
    public ResponseResult QueryDevicePropertyStatus(@RequestBody QueryDevicePropertyStatusRequest request) throws Exception {
        request.setIotInstanceId(iotInstanceId);
        QueryDevicePropertyStatusResponse deviceStatus = client.queryDevicePropertyStatus(request);
        return ResponseResult.success(deviceStatus.getBody().getData());
    }

    @PostMapping("/QueryThingModelPublished")
    @ApiOperation(value = "查看指定产品的已发布物模型中的功能定义详情", notes = "查看指定产品的已发布物模型中的功能定义详情")
    public ResponseResult queryThingModelPublished(@RequestBody QueryThingModelPublishedRequest request) throws Exception {
        request.setIotInstanceId(iotInstanceId);
        QueryThingModelPublishedResponse response = client.queryThingModelPublished(request);
        return ResponseResult.success(response.getBody().getData());
    }

    @PostMapping("/RegisterDevice")
    @ApiOperation(value = "单个注册设备", notes = "单个注册设备")
    public ResponseResult registerDevice(@RequestBody DeviceDto deviceDto) throws Exception {
        deviceService.registerDevice(deviceDto);
        return ResponseResult.success();
    }

    @PostMapping("/UpdateDevice")
    @ApiOperation(value = "修改设备备注名称", notes = "批量修改设备备注名称")
    public ResponseResult batchUpdateDevice(@RequestBody DeviceDto deviceDto) throws Exception {
        deviceService.updateDevice(deviceDto);
        return ResponseResult.success();
    }

    @DeleteMapping("/DeleteDevice")
    @ApiOperation(value = "删除设备", notes = "删除设备")
    public ResponseResult deleteDevice(@RequestBody DeleteDeviceRequest request) throws Exception {
        deviceService.deleteDevice(request);
        return ResponseResult.success();
    }

}

