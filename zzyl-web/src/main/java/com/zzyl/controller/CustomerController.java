package com.zzyl.controller;

import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.service.MemberService;
import com.zzyl.vo.MemberVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 用户管理
 */
@Slf4j
@Api(tags = "客户管理")
@RestController
@RequestMapping("/c-user")
public class CustomerController {

    @Resource
    private MemberService memberService;


    /**
     * 分页查询客户信息
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param phone 手机号
     * @param nickname 昵称
     * @return 分页结果
     */
    @ApiOperation(value = "分页查询客户信息", notes = "根据页码、每页数量、手机号、昵称分页查询客户信息")
    @GetMapping("page")
    public ResponseResult<PageResponse<MemberVo>> page(Integer pageNum, Integer pageSize, String phone, String nickname) {
        PageResponse<MemberVo> pageResponse = memberService.page(pageNum, pageSize, phone, nickname);
        return ResponseResult.success(pageResponse);
    }
}
