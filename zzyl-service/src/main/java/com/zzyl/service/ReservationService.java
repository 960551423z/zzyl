package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.dto.ReservationDto;
import com.zzyl.vo.ReservationVo;
import com.zzyl.vo.TimeCountVo;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    /**
     * 添加预约信息
     * @param dto 预约信息
     */
    void add(ReservationDto dto);

    /**
     * 更新预约信息
     * @param id 预约信息id
     * @param dto 预约信息
     */
    void update(Long id, ReservationDto dto);

    /**
     * 取消预约
     */
    void cancelReservation(Long id);

    /**
     * 根据id删除预约信息
     * @param id 预约信息id
     */
    void deleteById(Long id);

    /**
     * 根据id查找预约信息
     * @param id 预约信息id
     * @return 预约信息
     */
    ReservationVo findById(Long id);

    /**
     * 查找所有预约信息
     * @return 所有预约信息
     * @param mobile
     * @param time
     */
    List<ReservationVo> findAll(String mobile, LocalDateTime time);

    /**
     * 分页查找预约信息
     * @param page 页码
     * @param size 每页大小
     * @param name 预约人姓名
     * @param phone 预约人手机号
     * @param status 预约状态
     * @param type 预约类型
     * @param startTime
     * @param endTime
     * @return 预约信息列表
     */
    PageResponse<ReservationVo> findByPage(int page, int size, String name, String phone, Integer status, Integer type, LocalDateTime startTime, LocalDateTime endTime);


    /**
     * 如果预约未完成，则将预约状态更新为取消
     * @param id 预约信息id
     */
    void updateVisitReservationStatusToExpiredIfNotCompleted(Long id);

    /**
     * 查询每个时间段剩余预约次数
     * @param of 时间 日
     * @return 每个时间段剩余预约次数
     */
    public List<TimeCountVo> countReservationsForEachTimeWithinTimeRange(LocalDateTime of);

    /**
     * 取消预约次数的接口
     * @param updateBy 预约人
     * @return 取消预约次数
     */
    int getCancelledReservationCount(Long updateBy);

    /**
     * 来访
     * @param id ID
     * @param time 时间
     */
    void visit(Long id, Long time);

    void updateReservationStatus(LocalDateTime now);
}
