package com.zzyl.controller;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.dto.PostDto;
import com.zzyl.service.PostService;
import com.zzyl.vo.PostVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位前端控制器
 */
@Slf4j
@Api(tags = "岗位管理")
@RestController
@RequestMapping("post")
public class PostController {

    @Autowired
    PostService postService;

    /***
     *  多条件查询岗位分页列表
     * @param postDto 岗位Vo查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: PageResponse<PostVo>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "岗位分页",notes = "岗位分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "postDto",value = "岗位DTO对象",required = true,dataType = "PostDto"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"postDto.deptNo","postDto.dataState","postDto.postName"})
    public ResponseResult<PageResponse<PostVo>> findPostVoPageResponse(
                                    @RequestBody PostDto postDto,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        PageResponse<PostVo> pageResponse = postService.findPostPage(postDto, pageNum, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     *  保存岗位
     * @param postDto 岗位DTO对象
     * @return PostDto
     */
    @PutMapping
    @ApiOperation(value = "岗位添加",notes = "岗位添加")
    @ApiImplicitParam(name = "postDto",value = "岗位DTO对象",required = true,dataType = "PostDto")
    @ApiOperationSupport(includeParameters = {"postDto.deptNo","postDto.dataState","postDto.postName","postDto.remark"})
    public ResponseResult<PostVo> createPost(@RequestBody PostDto postDto) {
        PostVo postVoResult = postService.createPost(postDto);
        return ResponseResult.success(postVoResult);
    }

    /**
     *  修改岗位
     * @param postDto 岗位DTO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "岗位修改",notes = "岗位修改")
    @ApiImplicitParam(name = "postDto",value = "岗位DTO对象",required = true,dataType = "PostDto")
    @ApiOperationSupport(includeParameters = {
            "postDto.deptNo",
            "postDto.dataState",
            "postDto.postName",
            "postDto.remark",
            "postDto.id"})
    public ResponseResult<Boolean> updatePost(@RequestBody PostDto postDto) {
        Boolean flag = postService.updatePost(postDto);
        return ResponseResult.success(flag);
    }

    /**
     * 删除岗位
     */
    @ApiOperation("删除岗位")
    @DeleteMapping("/{postIds}")
    public ResponseResult remove(@PathVariable String[] postIds) {
        return ResponseResult.success(postService.deletePostByIds(postIds));
    }

    /***
     *  多条件查询岗位列表
     * @param postDto 岗位DTO对象
     * @return List<PostVo>
     */
    @PostMapping("list")
    @ApiOperation(value = "岗位列表",notes = "岗位列表")
    @ApiImplicitParam(name = "postDto",value = "岗位DTO对象",required = true,dataType = "PostDto")
    @ApiOperationSupport(includeParameters = {"postDto.deptNo"})
    public ResponseResult<List<PostVo>> postList(@RequestBody PostDto postDto) {
        List<PostVo> postVoList = postService.findPostList(postDto);
        return ResponseResult.success(postVoList);
    }


}
