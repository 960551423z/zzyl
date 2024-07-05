package com.zzyl.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.dto.VisitDto;
import com.zzyl.service.VisitService;
import com.zzyl.utils.ObjectUtil;
import com.zzyl.vo.VisitVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visit")
@Api(tags = "来访管理")
public class VisitController extends BaseController {

    @Autowired
    private VisitService visitService;

    @PostMapping
    @ApiOperation("新增来访")
    public ResponseResult add(@RequestBody VisitDto dto) {
        visitService.add(dto);
        return success();
    }

    @PutMapping("/{id}")
    @ApiOperation("更新来访")
    public ResponseResult update(@PathVariable Long id, @RequestBody VisitDto dto) {
        visitService.update(id, dto);
        return success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除来访")
    public ResponseResult deleteById(@PathVariable Long id) {
        visitService.deleteById(id);
        return success();
    }

    @PutMapping("/{id}/cancel")
    @ApiOperation("取消来访")
    public ResponseResult cancel(@PathVariable Long id) {
        visitService.cancelVisit(id);
        return success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询来访")
    public ResponseResult<VisitVo> findById(@PathVariable Long id) {
        VisitVo visitVO = visitService.findById(id);
        return success(visitVO);
    }

    @GetMapping
    @ApiOperation("查询所有来访")
    public ResponseResult<List<VisitVo>> findAll(@RequestParam(required = false) String mobile, @RequestParam(required = false) Long time) {
        List<VisitVo> visitVoList = visitService.findAll(mobile, LocalDateTimeUtil.of(time));
        return success(visitVoList);
    }

    /*
     *分页查询增加来访人姓名，手机号，状态，类型的查询条件
     */
    @GetMapping("/page")
    @ApiOperation("分页查询来访")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "来访人姓名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "long", paramType = "query")
    })
    public ResponseResult<PageResponse<VisitVo>> findByPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize,
                                                            @RequestParam(required = false) String name,
                                                            @RequestParam(required = false) String phone,
                                                            @RequestParam(required = false) Integer status,
                                                            @RequestParam(required = false) Integer type,
                                                            @RequestParam(required = false) Long startTime,
                                                            @RequestParam(required = false) Long endTime) {
        PageResponse<VisitVo> byPage = visitService.findByPage(pageNum, pageSize, name, phone, status, type, ObjectUtil.isEmpty(startTime)? null : LocalDateTimeUtil.of(startTime), ObjectUtil.isEmpty(endTime)? null : LocalDateTimeUtil.of(endTime));
        return success(byPage);
    }

}
