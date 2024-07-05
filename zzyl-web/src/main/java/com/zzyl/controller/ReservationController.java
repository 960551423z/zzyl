package com.zzyl.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.dto.ReservationDto;
import com.zzyl.service.ReservationService;
import com.zzyl.utils.ObjectUtil;
import com.zzyl.vo.ReservationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
@Api(tags = "预约管理")
public class ReservationController extends BaseController {


    @Autowired
    private ReservationService reservationService;

    @PostMapping
    @ApiOperation("新增预约")
    public ResponseResult add(@RequestBody ReservationDto dto) {
        reservationService.add(dto);
        return success();
    }

    @PutMapping("/{id}")
    @ApiOperation("更新预约")
    public ResponseResult update(@PathVariable Long id, @RequestBody ReservationDto dto) {
        reservationService.update(id, dto);
        return success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除预约")
    public ResponseResult deleteById(@PathVariable Long id) {
        reservationService.deleteById(id);
        return success();
    }

    @PutMapping("/{id}/cancel")
    @ApiOperation("取消预约")
    public ResponseResult cancel(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return success();
    }

    @PutMapping("/{id}/visit")
    @ApiOperation("预约到访")
    public ResponseResult visit(@PathVariable Long id, @RequestParam(required = false) Long time) {
        reservationService.visit(id, time);
        return success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询预约")
    public ResponseResult<ReservationVo> findById(@PathVariable Long id) {
        ReservationVo reservationVO = reservationService.findById(id);
        return success(reservationVO);
    }

    @GetMapping
    @ApiOperation("查询所有预约")
    public ResponseResult<List<ReservationVo>> findAll(@RequestParam(required = false) String mobile, @RequestParam(required = false) Long time) {
        List<ReservationVo> reservationVoList = reservationService.findAll(mobile, LocalDateTimeUtil.of(time));
        return success(reservationVoList);
    }

    @GetMapping("/countByTime")
    @ApiOperation("查询每个时间段剩余预约次数")
    public ResponseResult countReservationsForEachTimeWithinTimeRange(@RequestParam(required = false) Long time) {
        return success(reservationService.countReservationsForEachTimeWithinTimeRange(LocalDateTimeUtil.of(time)));

    }

    /*
     *分页查询增加预约人姓名，手机号，状态，类型的查询条件
     */
    @GetMapping("/page")
    @ApiOperation("分页查询预约")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "预约人姓名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "long", paramType = "query")
    })
    public ResponseResult<PageResponse<ReservationVo>> findByPage(@RequestParam(defaultValue = "1") int pageNum,
                                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                                  @RequestParam(required = false) String name,
                                                                  @RequestParam(required = false) String phone,
                                                                  @RequestParam(required = false) Integer status,
                                                                  @RequestParam(required = false) Integer type,
                                                                  @RequestParam(required = false) Long startTime,
                                                                  @RequestParam(required = false) Long endTime) {
        PageResponse<ReservationVo> byPage = reservationService.findByPage(pageNum, pageSize, name, phone, status, type, ObjectUtil.isEmpty(startTime)? null : LocalDateTimeUtil.of(startTime), ObjectUtil.isEmpty(endTime)? null : LocalDateTimeUtil.of(endTime));
        return success(byPage);
    }


}
