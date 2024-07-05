package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.dto.UserLoginRequestDto;
import com.zzyl.entity.Member;
import com.zzyl.vo.LoginVo;
import com.zzyl.vo.MemberVo;

import java.io.IOException;

/**
 * 用户管理
 */
public interface MemberService {

    /**
     * 新增
     *
     * @param member 用户信息
     */
    void save(Member member);

    /**
     * 根据openid查询用户
     *
     * @param openid 微信ID
     * @return 用户信息
     */
    Member getByOpenid(String openid);

    /**
     * 根据id查询用户
     *
     * @param id 用户id
     * @return 用户信息
     */
    Member getById(Long id);

    /**
     * 登录
     *
     * @param userLoginRequestDto 登录code
     * @return 用户信息
     * @throws IOException IO异常
     */
    LoginVo login(UserLoginRequestDto userLoginRequestDto) throws IOException;

    /**
     * 更新用户信息
     *
     * @param member 用户信息
     * @return 更新结果
     */
    int update(Member member);

    /**
     * 分页查询用户列表
     *
     * @param page     当前页码
     * @param pageSize 每页数量
     * @param phone    手机号
     * @param nickname 昵称
     * @return 分页结果
     */
    PageResponse<MemberVo> page(Integer page, Integer pageSize, String phone, String nickname);

}
