package com.zzyl.mapper;

import com.github.pagehelper.Page;
import com.zzyl.entity.MemberElder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户老人关联 Mapper
 */
@Mapper
public interface MemberElderMapper {

    /**
     * 新增客户老人关联记录
     *
     * @param memberElder 客户老人关联实体类
     * @return 受影响的行数
     */
    int add(MemberElder memberElder);

    /**
     * 根据id更新客户老人关联记录
     *
     * @param memberElder 客户老人关联实体类
     * @return 受影响的行数
     */
    int update(MemberElder memberElder);

    /**
     * 根据id删除客户老人关联记录
     *
     * @param id 客户老人关联id
     * @return 受影响的行数
     */
    int deleteById(Long id);

    /**
     * 根据id查询客户老人关联记录
     *
     * @param id 客户老人关联id
     * @return 客户老人关联实体类
     */
    MemberElder getById(Long id);

    /**
     * 根据客户id查询客户老人关联记录列表
     *
     * @param memberId 客户id
     * @return 客户老人关联实体类列表
     */
    List<MemberElder> listByMemberId(Long memberId);

    /**
     * 根据老人id查询客户老人关联记录列表
     *
     * @param elderId 老人id
     * @return 客户老人关联实体类列表
     */
    List<MemberElder> listByElderId(Long elderId);


    Page<List<MemberElder>> listByCondition(@Param("memberId") Long memberId, @Param("elderId") Long elderId); // 根据客户id和老人id查询客户老人关联记录列表
}

