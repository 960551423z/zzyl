package com.zzyl.service;


import com.zzyl.base.PageResponse;
import com.zzyl.dto.NursingLevelDto;
import com.zzyl.entity.NursingLevel;
import com.zzyl.vo.NursingLevelVo;

import java.util.List;

public interface NursingLevelService {
    /**
     * 查询全部护理级别
     * @return 全部护理级别
     */
    List<NursingLevelVo> listAll();

    /**
     * 新增护理级别
     * @param nursingLevel 护理级别
     */
    void insert(NursingLevelDto nursingLevel);

    /**
     * 批量插入护理级别
     * @param nursingLevels 护理级别集合
     */
    void insertBatch(List<NursingLevelDto> nursingLevels);

    /**
     * 根据ID查询护理级别
     * @param id 护理级别ID
     * @return 护理级别
     */
    NursingLevelVo getById(Long id);

    /**
     * 更新护理级别
     * @param nursingLevel 护理级别
     */
    void update(NursingLevelDto nursingLevel);

    /**
     * 删除护理级别
     * @param id 护理级别ID
     */
    void delete(Long id);


    /**
     * 分页查询护理级别列表
     * @param pageNum 页码
     * @param pageSize 页面大小
     * @param name 护理级别名称
     * @param status 护理级别状态
     * @return 护理级别分页结果
     */
    PageResponse<NursingLevelVo> listByPage(Integer pageNum, Integer pageSize, String name, Integer status);



    /**
     * 启用或禁用
     * @param id ID
     * @param status 状态
     */
    void enableOrDisable(Long id, Integer status);

    /**
     * 根据计划id列表查询护理等级
     * @param ids
     * @return
     */
    List<NursingLevel> listAllByPlanIds(List<Long> ids);
}
