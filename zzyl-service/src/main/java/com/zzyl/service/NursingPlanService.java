package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.dto.NursingPlanDto;
import com.zzyl.vo.NursingPlanVo;

import java.util.List;

public interface NursingPlanService {

    /**
     * 增加护理计划
     */
    void add(NursingPlanDto nursingPlan);

    /**
     * 更新护理计划
     */
    void update(NursingPlanDto nursingPlan);

    /**
     * 根据id删除护理计划
     */
    void deleteById(Long id);

    /**
     * 根据id查询护理计划
     */
    NursingPlanVo getById(Long id);

    /**
     * 查询所有护理计划
     */
    List<NursingPlanVo> listAll();

    /**
     * 分页查询护理计划
     * @param name 护理计划名称
     * @param status 护理计划状态
     * @param pageNum 当前页码
     * @param pageSize 每页显示的记录数
     * @return 分页查询结果
     */
    PageResponse<NursingPlanVo> listByPage(String name, Integer status, Integer pageNum, Integer pageSize);

        /**
     * 启用或禁用
     * @param id ID
     * @param status 状态
     */
    void enableOrDisable(Long id, Integer status);
}

