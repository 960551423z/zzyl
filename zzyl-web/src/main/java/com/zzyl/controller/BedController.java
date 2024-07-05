package com.zzyl.controller;

import com.zzyl.base.ResponseResult;
import com.zzyl.dto.BedDto;
import com.zzyl.service.BedService;
import com.zzyl.vo.BedVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bed")
@Api(tags = "床位管理相关接口")
public class BedController extends BaseController {

    @Autowired
    private BedService bedService;

    @PostMapping("/create")
    @ApiOperation(value = "创建床位", notes = "传入床位对象，包括床位号和所属房间号")
    public ResponseResult createBed(@ApiParam(value = "床位信息", required = true) @RequestBody BedDto bed) {
        bedService.addBed(bed);
        return success("Bed created successfully");
    }

    @GetMapping("/read/{id}")
    @ApiOperation(value = "根据id查询床位", notes = "传入床位id")
    public ResponseResult readBed(
            @ApiParam(value = "床位ID", required = true) @PathVariable("id") Long id) {
        BedVo bed = bedService.getBedById(id);
        return success(bed);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新床位", notes = "传入床位对象，包括床位id、床位号、所属房间号等信息")
    public ResponseResult updateBed(
            @ApiParam(value = "床位信息", required = true) @RequestBody BedDto bed) {
        bedService.updateBed(bed);
        return success("Bed updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除床位", notes = "传入床位id")
    public ResponseResult deleteBed(
            @ApiParam(value = "床位ID", required = true) @PathVariable("id") Long id) {
        bedService.deleteBedById(id);
        return success("Bed deleted successfully");
    }

    @GetMapping("/read/room/{roomId}")
    @ApiOperation(value = "根据房间id查询床位", notes = "传入房间id")
    public ResponseResult<List<BedVo>> readBedByRoomId(
            @ApiParam(value = "房间ID", required = true) @PathVariable("roomId") Long roomId) {
        List<BedVo> beds = bedService.getBedsByRoomId(roomId);
        return success(beds);
    }
}
