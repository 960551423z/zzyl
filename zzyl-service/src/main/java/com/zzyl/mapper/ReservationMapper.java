package com.zzyl.mapper;

import com.github.pagehelper.Page;
import com.zzyl.entity.Reservation;
import com.zzyl.vo.TimeCountVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReservationMapper {

    int insert(Reservation reservation);

    int update(Reservation reservation);

    int deleteById(Long id);

    Reservation findById(Long id);

    List<Reservation> findAll(@Param("createBy") Long userId, @Param("mobile") String mobile, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    Page<Reservation> findByPage(@Param("page") int startIndex, @Param("pageSize") int pageSize, @Param("name") String name, @Param("mobile") String mobile, @Param("status") Integer status, @Param("type") Integer type, @Param("createBy") Long userId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    int countReservationsWithinTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("createBy") Long createBy, @Param("status") Integer status);

    List<TimeCountVo> countReservationsForEachTimeWithinTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    //    int countCancelledReservationsWithinTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("updateBy") Long updateBy);
    int countCancelledReservationsWithinTimeRange(LocalDateTime startTime, LocalDateTime endTime, Long updateBy);

    /**
     * 当前时间大于数据库中存储的预约时间，那我就执行这个
     * @param minusDays 当前时间
     */
    @Update("update reservation set status = 3 where status = 0 and time <= #{minusDays}")
    void updateReservationStatus(LocalDateTime minusDays);

    Page<Reservation> page(Integer pageNum, Integer pageSize, Integer status, Long userId);
}
