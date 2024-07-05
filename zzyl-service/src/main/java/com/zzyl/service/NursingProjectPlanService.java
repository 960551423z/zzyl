package com.zzyl.service;

import com.zzyl.dto.NursingProjectPlanDto;
import com.zzyl.vo.NursingProjectPlanVo;

import java.util.List;

/**
 * NursingProjectPlanService接口定义了对护理项目计划的各种操作方法
 * 可以向数据库中增加、更新、删除、查询护理项目计划
 * 也可以获取所有的护理项目计划以及批量增加护理项目计划
 */
public interface NursingProjectPlanService {

    /**
     * 增加护理项目计划
     * @param nursingProjectPlan 护理项目计划数据传输对象
     * @return 受影响的行数
     */
    int add(NursingProjectPlanDto nursingProjectPlan);

    /**
     * 更新护理项目计划
     * @param nursingProjectPlan 护理项目计划数据传输对象
     * @return 受影响的行数
     */
    int update(NursingProjectPlanDto nursingProjectPlan);

    /**
     * 根据IDs删除护理项目计划
     * @param ids 护理项目计划IDs
     * @return 受影响的行数
     */
    int deleteByIds(List<Long> ids);

    /**
     * 根据ID查询护理项目计划
     * @param id 护理项目计划ID
     * @return 护理项目计划视图对象
     */
    NursingProjectPlanVo getById(Long id);

    /**
     * 批量增加护理项目计划
     * @param projectPlans 护理项目计划数据传输对象列表
     * @return 受影响的行数
     */
    int addList(List<NursingProjectPlanDto> projectPlans);
}
