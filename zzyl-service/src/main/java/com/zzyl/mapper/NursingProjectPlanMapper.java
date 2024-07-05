package com.zzyl.mapper;

import com.zzyl.entity.NursingProjectPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据库映射接口：NursingProjectPlanMapper（护理项目计划Mapper）
 */
@Mapper
public interface NursingProjectPlanMapper {

    /**
     * 插入护理项目计划记录
     * @param nursingProjectPlan 护理项目计划实体
     * @return 受影响的行数
     */
    int insert(NursingProjectPlan nursingProjectPlan);

    /**
     * 更新护理项目计划记录
     * @param nursingProjectPlan 护理项目计划实体
     * @return 受影响的行数
     */
    int update(NursingProjectPlan nursingProjectPlan);

    /**
     * 根据ID删除护理项目计划记录
     * @param ids 记录IDs
     * @return 受影响的行数
     */
    int deleteByIds(List<Long> ids);

    /**
     * 根据ID获取护理项目计划记录
     * @param id ID
     * @return 护理项目计划实体
     */
    NursingProjectPlan getById(Long id);


    /**
     * 批量插入护理项目计划记录列表
     * @param projectPlans 护理项目计划实体列表
     * @return 受影响的行数
     */
    int insertList(@Param("list") List<NursingProjectPlan> projectPlans);

    /**
     * 根据项目IDs批量查询护理项目计划记录
     * @param ids 项目IDs
     * @return 护理项目计划列表
     */
    List<NursingProjectPlan> listAllByProjectIds(List<Long> ids);
}
