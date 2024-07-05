package com.zzyl.controller;

import com.zzyl.base.ResponseResult;
import com.zzyl.dto.RoomTypeDto;
import com.zzyl.service.RoomTypeService;
import com.zzyl.vo.RoomTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roomTypes")
@Api(tags = "房型管理")
public class RoomTypeController extends BaseController {
    @Autowired
    private RoomTypeService roomTypeService;

    @PostMapping
    @ApiOperation("添加房型")
    public ResponseResult addRoomType(@RequestBody RoomTypeDto roomTypeDTO) {
        roomTypeService.addRoomType(roomTypeDTO);
        return success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除房型")
    public ResponseResult removeRoomType(
            @ApiParam(value = "房型ID", required = true) @PathVariable Long id) {
        roomTypeService.removeRoomType(id);
        return success();
    }

    @PutMapping("/{id}")
    @ApiOperation("修改房型")
    public ResponseResult modifyRoomType(
            @ApiParam(value = "房型ID", required = true) @PathVariable Long id,
            @RequestBody RoomTypeDto roomTypeDTO) {
        roomTypeService.modifyRoomType(id, roomTypeDTO);
        return success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询房型")
    public ResponseResult<RoomTypeVo> findRoomTypeById(
            @ApiParam(value = "房型ID", required = true) @PathVariable Long id) {
        RoomTypeVo roomTypeVO = roomTypeService.findRoomTypeById(id);
        return success(roomTypeVO);
    }

    @GetMapping
    @ApiOperation("查询所有房型")
    public ResponseResult<List<RoomTypeVo>> findRoomTypeList() {
        List<RoomTypeVo> roomTypeVoList = roomTypeService.findRoomTypeList();
        return success(roomTypeVoList);
    }

    @GetMapping("/status/{status}")
    @ApiOperation("根据状态查询房型")
    public ResponseResult<List<RoomTypeVo>> findRoomTypeListByStatus(
            @ApiParam(value = "房型状态", required = true) @PathVariable Integer status) {
        List<RoomTypeVo> roomTypeVoList = roomTypeService.findRoomTypeListByStatus(status);
        return success(roomTypeVoList);
    }

    @GetMapping("/typeName/{typeName}")
    @ApiOperation("根据类型名查询房型")
    public ResponseResult<List<RoomTypeVo>> findRoomTypeListByTypeName(
            @ApiParam(value = "房型类型名", required = true) @PathVariable String typeName) {
        List<RoomTypeVo> roomTypeVoList = roomTypeService.findRoomTypeListByTypeName(typeName);
        return success(roomTypeVoList);
    }

    @PutMapping("/{id}/status/{status}")
    @ApiOperation("启用/禁用房型")
    public ResponseResult enableOrDisable(
            @ApiParam(value = "房型ID", required = true) @PathVariable Long id,
            @ApiParam(value = "房型状态", required = true) @PathVariable Integer status) {
        roomTypeService.enableOrDisable(id, status);
        return success();
    }
}
