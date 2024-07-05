package com.zzyl.mapper;

import com.github.pagehelper.Page;
import com.zzyl.entity.NursingPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据库映射接口：NursingPlanMapper（护理计划Mapper）
 */
@Mapper
public interface NursingPlanMapper {

    /**
     * 插入护理计划实体
     * @param nursingPlan 护理计划实体
     */
    void insert(NursingPlan nursingPlan);

    /**
     * 更新护理计划实体
     * @param nursingPlan 护理计划实体
     */
    void update(NursingPlan nursingPlan);

    /**
     * 根据ID删除护理计划实体
     * @param id ID
     */
    void deleteById(Long id);

    /**
     * 根据ID查询护理计划实体
     * @param id ID
     * @return 护理计划实体
     */
    NursingPlan getById(Long id);

    /**
     * 查询所有护理计划实体
     * @return 护理计划实体列表
     */
    List<NursingPlan> listAll();

    /**
     * 根据名称和状态分页查询护理计划实体
     * @param page 页码
     * @param pageSize 页面大小
     * @param name 计划名称
     * @param status 状态
     * @return 分页的护理计划实体列表
     */
    Page<List<NursingPlan>> listByPage(@Param("page") int page, @Param("pageSize") int pageSize, @Param("name") String name, @Param("status") Integer status);

    /**
     * 启用或禁用护理计划
     * @param id ID
     * @param status 状态，0：禁用，1：启用
     */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
