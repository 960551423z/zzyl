
package com.zzyl.controller.customer;

import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.dto.MemberElderDto;
import com.zzyl.service.MemberElderService;
import com.zzyl.vo.MemberElderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户老人关联 Controller
 */
@RestController
@RequestMapping("/customer/memberElder")
@Api(tags = "客户老人关联")
public class MemberElderController {

    @Autowired
    private MemberElderService memberElderService;

    /**
     * 新增客户老人关联记录
     *
     * @param memberElderDto 客户老人关联 DTO
     * @return 操作结果
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增客户老人关联记录")
    public ResponseResult add(@RequestBody @Validated MemberElderDto memberElderDto) {
        memberElderService.add(memberElderDto);
        return ResponseResult.success();
    }

    /**
     * 根据id更新客户老人关联记录
     *
     * @param memberElderDto 客户老人关联 DTO
     * @return 操作结果
     */
    @PostMapping("/update")
    @ApiOperation(value = "根据id更新客户老人关联记录")
    public ResponseResult update(@RequestBody @Validated MemberElderDto memberElderDto) {
        memberElderService.update(memberElderDto);
        return ResponseResult.success();
    }

    /**
     * 根据id删除客户老人关联记录
     *
     * @param id 客户老人关联id
     * @return 操作结果
     */
    @DeleteMapping("/deleteById")
    @ApiOperation(value = "根据id删除客户老人关联记录")
    public ResponseResult deleteById(@RequestParam Long id) {
        memberElderService.deleteById(id);
        return ResponseResult.success();
    }

    /**
     * 根据id查询客户老人关联记录
     *
     * @param id 客户老人关联id
     * @return 客户老人关联实体类
     */
    @GetMapping("/getById")
    @ApiOperation(value = "根据id查询客户老人关联记录")
    public ResponseResult getById(@RequestParam Long id) {
        MemberElderVo memberElder = memberElderService.getById(id);
        return ResponseResult.success(memberElder);
    }

    /**
     * 我的家人列表
     *
     * @return 客户老人关联实体类
     */
    @GetMapping("/my")
    @ApiOperation(value = "我的家人列表")
    public ResponseResult my() {
        List<MemberElderVo> memberElders = memberElderService.my();
        return ResponseResult.success(memberElders);
    }

    /**
     * 分页查询客户老人关联记录
     *
     * @param memberId 客户id
     * @param elderId  老人id
     * @param pageNum  当前页码
     * @param pageSize 每页记录数
     * @return 客户老人关联记录列表
     */
    @GetMapping("/list-by-page")
    @ApiOperation(value = "分页查询客户老人关联记录")
    public ResponseResult<PageResponse<MemberElderVo>> listByPage(Long memberId, Long elderId, Integer pageNum, Integer pageSize) {
        PageResponse<MemberElderVo> pageInfo = memberElderService.listByPage(memberId, elderId, pageNum, pageSize);
        return ResponseResult.success(pageInfo);
    }

}


