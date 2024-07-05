package com.zzyl.controller;

import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.config.OSSAliyunFileStorageService;
import com.zzyl.dto.NursingProjectDto;
import com.zzyl.service.NursingProjectService;
import com.zzyl.vo.NursingProjectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: 阿庆
 * @date: 2024/7/4 上午8:33
 */

@RequestMapping("/nursing_project")
@RestController
@Api(tags = "护理项目")
public class NursingProjectController extends BaseController {

    @Autowired
    private NursingProjectService nursingProjectService;

    @Autowired
    private OSSAliyunFileStorageService aliyunFileStorageService;

    @GetMapping()
    @ApiOperation("分页查询+条件查询")
    public ResponseResult getByPage(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "status", required = false) Integer status
    ) {
        PageResponse<NursingProjectVo> page =
                nursingProjectService.getByPage(name, pageNum, pageSize, status);

        return success(page);
    }


    @PostMapping
    @ApiOperation("添加护理项目")
    public ResponseResult add(@RequestBody NursingProjectDto nursingProjectDto) {

        nursingProjectService.add(nursingProjectDto);

        return success();
    }


    @GetMapping("/{id}")
    @ApiOperation("根据id查询护理项目")
    public ResponseResult<NursingProjectVo> getById(
            @PathVariable("id") Long id) {
        NursingProjectVo nursingProjectVo = nursingProjectService.getById(id);
        return success(nursingProjectVo);
    }

    @PutMapping
    @ApiOperation("修改护理项目")
    public ResponseResult update(@RequestBody NursingProjectDto nursingProjectDto) {
        nursingProjectService.update(nursingProjectDto);
        return success();
    }

    @PutMapping("/{id}/status/{status}")
    @ApiOperation("启用禁用")
    public ResponseResult updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        nursingProjectService.updateByStatus(id,status);
        return success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除护理项目")
    public ResponseResult delete(@PathVariable Long id) {
        NursingProjectVo nursingProjectVo = nursingProjectService.getById(id);
        String image = nursingProjectVo.getImage();
        aliyunFileStorageService.delete(image);
        nursingProjectService.removeById(id);
        return success();
    }

}
