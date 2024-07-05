package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.entity.NursingTask;
import com.zzyl.vo.NursingTaskVo;
import com.zzyl.vo.retreat.ElderVo;

import java.time.LocalDateTime;
import java.util.List;

public interface NursingTaskService {

    /**
     * 新增护理任务
     * @param record
     * @return
     */
    int insert(NursingTask record);


    /**
     * 批量新增护理任务
     * @param list
     * @return
     */
    int batchInsert(List<NursingTask> list);

    /**
     * 根据id查询护理任务
     * @param id
     * @return
     */
    NursingTaskVo selectByPrimaryKey(Long id);

    /**
     * 取消任务
     * @param taskId 任务ID
     */
    void cancelTask(Long taskId, String reason);

    /**
     * 任务改期
     * @param taskId 任务ID
     * @param newTime 新的执行时间
     */
    void rescheduleTask(Long taskId, LocalDateTime newTime);

    /**
     * 任务执行
     * @param taskId 任务ID
     */
    void executeTask(Long taskId, LocalDateTime newTime, String image, String mark);

    /**
     * 分页查询任务
     * @param page 页码
     * @param size 每页大小
     * @param nurseName
     * @return 任务列表
     */
    PageResponse<NursingTaskVo> getTasksByPage(int page, int size, String elderName, Long nurseName, Long projectId, LocalDateTime startTime, LocalDateTime endTime, Integer status);

    /**
     * 生成护理任务
     * @param elderVo
     * @param signDate
     * @param startTime
     */
    void createMonthTask(ElderVo elderVo, LocalDateTime signDate, LocalDateTime startTime);

}