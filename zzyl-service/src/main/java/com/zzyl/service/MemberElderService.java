package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.dto.MemberElderDto;
import com.zzyl.vo.MemberElderVo;

import java.util.List;

/**
 * 客户老人关联 Service
 */
public interface MemberElderService {

    /**
     * 新增客户老人关联记录
     *
     * @param memberElder 客户老人关联实体类
     */
    void add(MemberElderDto memberElder);

    /**
     * 根据id更新客户老人关联记录
     *
     * @param memberElder 客户老人关联实体类
     */
    void update(MemberElderDto memberElder);

    /**
     * 根据id删除客户老人关联记录
     *
     * @param id 客户老人关联id
     */
    void deleteById(Long id);

    /**
     * 根据id查询客户老人关联记录
     *
     * @param id 客户老人关联id
     * @return 客户老人关联实体类
     */
    MemberElderVo getById(Long id);

    /**
     * 根据客户id查询客户老人关联记录列表
     *
     * @param memberId 客户id
     * @return 客户老人关联实体类列表
     */
    List<MemberElderVo> listByMemberId(Long memberId);

    /**
     * 根据老人id查询客户老人关联记录列表
     *
     * @param elderId 老人id
     * @return 客户老人关联实体类列表
     */
    List<MemberElderVo> listByElderId(Long elderId);

    /**
     * 分页查询客户老人关联记录
     *
     * @param memberId   客户id
     * @param elderId    老人id
     * @param pageNum    当前页码
     * @param pageSize   每页记录数
     * @return 客户老人关联记录列表
     */
    PageResponse<MemberElderVo> listByPage(Long memberId, Long elderId, Integer pageNum, Integer pageSize);

    /**
     * 根据当前登录人查询老人与客户关系
     * @return
     */
    List<MemberElderVo> my();

}

