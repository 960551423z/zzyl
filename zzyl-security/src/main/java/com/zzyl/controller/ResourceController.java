package com.zzyl.controller;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zzyl.base.ResponseResult;
import com.zzyl.dto.ResourceDto;
import com.zzyl.service.ResourceService;
import com.zzyl.vo.MenuVo;
import com.zzyl.vo.ResourceVo;
import com.zzyl.vo.TreeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资源前端控制器
 */
@Slf4j
@Api(tags = "资源管理")
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    ResourceService resourceService;

    /**
     * @param resourceDto 资源Vo对象
     * @return ResourceVo
     *  保存资源
     */
    @PutMapping
    @ApiOperation(value = "资源添加", notes = "资源添加")
    @ApiImplicitParam(name = "resourceDto", value = "资源DTO对象", required = true, dataType = "ResourceDto")
    @ApiOperationSupport(includeParameters = {"resourceDto.dataState"
            , "resourceDto.icon"
            , "resourceDto.parentResourceNo"
            , "resourceDto.requestPath"
            , "resourceDto.resourceName"
            , "resourceDto.resourceType"
            , "resourceDto.sortNo"})
    public ResponseResult<ResourceVo> createResource(@RequestBody ResourceDto resourceDto) {
        return ResponseResult.success(resourceService.createResource(resourceDto));
    }

    /**
     * @param resourceDto 资源DTO对象
     * @return Boolean 是否修改成功
     *  修改资源
     */
    @PatchMapping
    @ApiOperation(value = "资源修改", notes = "资源修改")
    @ApiImplicitParam(name = "resourceDto", value = "资源DTO对象", required = true, dataType = "ResourceDto")
    @ApiOperationSupport(includeParameters = {
            "resourceDto.id",
            "resourceDto.dataState"
            , "resourceDto.icon"
            , "resourceDto.parentResourceNo"
            , "resourceDto.requestPath"
            , "resourceDto.resourceName"
            , "resourceDto.resourceType"
            , "resourceDto.sortNo"})
    public ResponseResult<Boolean> updateResource(@RequestBody ResourceDto resourceDto) {
        return ResponseResult.success(resourceService.updateResource(resourceDto));
    }

    /**
     * 删除菜单
     */
    @ApiOperation("删除菜单")
    @DeleteMapping("/{menuId}")
    public ResponseResult remove(@PathVariable("menuId") String menuId) {
        return ResponseResult.success(resourceService.deleteMenuById(menuId));
    }

    /***
     *  多条件查询资源列表
     * @param resourceDto 资源Vo对象
     * @return List<ResourceVo>
     */
    @PostMapping("/list")
    @ApiOperation(value = "资源列表", notes = "资源列表")
    @ApiImplicitParam(name = "resourceDto", value = "资源DTO对象", required = true, dataType = "ResourceDto")
    @ApiOperationSupport(includeParameters = {"resourceDto.parentResourceNo", "resourceDto.resourceType"})
    public ResponseResult<List<ResourceVo>> resourceList(@RequestBody ResourceDto resourceDto) {
        List<ResourceVo> resourceVoList = resourceService.findResourceList(resourceDto);
        return ResponseResult.success(resourceVoList);
    }

    /**
     * @param resourceDto 资源对象
     * @return
     *  资源树形
     */
    @PostMapping("/tree")
    @ApiOperation(value = "资源树形", notes = "资源树形")
    @ApiImplicitParam(name = "resourceDto", value = "资源DTO对象", required = true, dataType = "ResourceDto")
    @ApiOperationSupport(includeParameters = {"resourceDto.label"})
    public ResponseResult<TreeVo> resourceTreeVo(@RequestBody ResourceDto resourceDto) {
        TreeVo treeVo = resourceService.resourceTreeVo(resourceDto);
        return ResponseResult.success(treeVo);
    }

    /**
     * @return
     *  左侧菜单
     */
    @GetMapping("/menus")
    @ApiOperation(value = "左侧菜单", notes = "左侧菜单")
    public ResponseResult<List<MenuVo>> menus() {
        List<MenuVo> menus = resourceService.menus();
        return ResponseResult.success(menus);
    }

    /**
     * @return
     *  按钮列表
     */
    @GetMapping("/myButten")
    @ApiOperation(value = "我的按钮列表", notes = "我的按钮列表")
    public ResponseResult<List<MenuVo>> buttons() {
        List<MenuVo> menus = resourceService.buttons();
        return ResponseResult.success(menus);
    }

}
