package com.zzyl.controller;

import com.zzyl.base.ResponseResult;
import com.zzyl.dto.FloorDto;
import com.zzyl.service.FloorService;
import com.zzyl.vo.FloorVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/floor")
@Api(tags = "楼层管理")
public class FloorController extends BaseController {

    @Autowired
    private FloorService floorService;

    @PostMapping("/add")
    @ApiOperation(value = "添加楼层", notes = "传入楼层信息，返回添加结果")
    public ResponseResult addFloor(@RequestBody FloorDto floorDto) {
        floorService.addFloor(floorDto);
        return ResponseResult.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除楼层", notes = "根据楼层id删除指定楼层，返回删除结果")
    public ResponseResult deleteFloor(@PathVariable Long id) {
        floorService.deleteFloor(id);
        return ResponseResult.success();
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新楼层", notes = "传入更新的楼层信息，返回更新结果")
    public ResponseResult updateFloor(@RequestBody FloorDto floorDto) {
        floorService.updateFloor(floorDto);
        return ResponseResult.success();
    }

    @GetMapping("/get/{id}")
    @ApiOperation(value = "获取楼层", notes = "根据楼层id获取指定楼层，返回楼层信息")
    public ResponseResult<FloorVo> getFloor(@PathVariable Long id) {
        return ResponseResult.success(floorService.getFloor(id));
    }

    @GetMapping("/getAll")
    @ApiOperation(value = "获取所有楼层", notes = "无需参数，获取所有楼层，返回楼层信息列表")
    public ResponseResult<List<FloorVo>> getAllFloors() {
        return ResponseResult.success(floorService.getAllFloors());
    }

    @GetMapping("/getAllFloorsWithDevice")
    @ApiOperation(value = "获取所有楼层 (智能床位)", notes = "无需参数，获取所有楼层，返回楼层信息列表")
    public ResponseResult<List<FloorVo>> getAllFloorsWithDevice() {
        return ResponseResult.success(floorService.selectAllByDevice());
    }

    @GetMapping("/getAllFloorsWithNur")
    @ApiOperation(value = "获取所有楼层 (负责老人)", notes = "无需参数，获取所有楼层，返回楼层信息列表")
    public ResponseResult<List<FloorVo>> getAllFloorsWithNur() {
        return ResponseResult.success(floorService.selectAllByNur());
    }

    @GetMapping("/getAllWithRoomAndBed")
    @ApiOperation(value = "获取所有楼层 （包含房间和床位）", notes = "无需参数，获取所有楼层，返回楼层信息列表")
    public ResponseResult<List<FloorVo>> getAllWithRoomAndBed() {
        return ResponseResult.success(floorService.getAllWithRoomAndBed());
    }
}
