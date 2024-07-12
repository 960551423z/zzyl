package com.zzyl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zzyl.base.PageResponse;
import com.zzyl.dto.ReservationDto;
import com.zzyl.dto.VisitDto;
import com.zzyl.entity.Reservation;
import com.zzyl.enums.ReservationStatus;
import com.zzyl.exception.BaseException;
import com.zzyl.mapper.ReservationMapper;
import com.zzyl.service.ElderService;
import com.zzyl.service.ReservationService;
import com.zzyl.service.VisitService;
import com.zzyl.utils.UserThreadLocal;
import com.zzyl.vo.ReservationVo;
import com.zzyl.vo.TimeCountVo;
import com.zzyl.vo.retreat.ElderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    @Resource
    private VisitService visitService;

    @Resource
    private ElderService elderService;

    /**
     * 添加预约
     */
    @Override
    public void add(ReservationDto dto) {

        // 取消找过三次不能预约
        Reservation reservation = BeanUtil.copyProperties(dto, Reservation.class);

        // 查询预约次数
        Long userId = UserThreadLocal.getUserId();
        int count = getCancelledReservationCount(userId);
        if (count >= 3)
            throw new BaseException("今天取消次数已达上限，不可进行预约");


        reservation.setStatus(ReservationStatus.PENDING.getOrdinal());
        reservation.setCreateBy(userId);

        // 预约过了，数据库设置了时间的唯一性
        try {
            reservationMapper.insert(reservation);
        }
        catch (Exception e) {
            log.info(e +"");
            throw new BaseException("此手机号已预约该时间");
        }

    }


    /**
     * 获取取消预约次数
     * @param updateBy 更新人id
     * @return 取消预约次数
     */
    @Override
    public int getCancelledReservationCount(Long updateBy) {

        // 取消预约次数（一天时间内，所以是从00:00:00 - 23:59:59）
        return reservationMapper.countCancelledReservationsWithinTimeRange(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0),
                LocalDateTime.now().withHour(23).withMinute(59).withSecond(59), updateBy);
    }


    /**
     * 查询每个时间段剩余预约次数
     * @param time 时间 日
     * @return 每个时间段剩余预约次数
     */
    @Override
    public List<TimeCountVo> countReservationsForEachTimeWithinTimeRange(LocalDateTime time) {

        // 查询的这个时间段往后推24小时，假如是今天则向后推24小时，明天也往后推24小时或者
//        LocalDateTime endTime = time.plusHours(24);
        LocalDateTime endTime = time.withHour(23).withMinute(59).withSecond(59);
        return reservationMapper.countReservationsForEachTimeWithinTimeRange(time, endTime);
    }


    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param status
     * @return
     */
    @Override
    public PageResponse<ReservationVo> page(Integer pageNum, Integer pageSize, Integer status) {

        PageHelper.startPage(pageNum,pageSize);

        // 当前登录人
        Long userId = UserThreadLocal.getUserId();


        Page<Reservation> pages = reservationMapper.page(pageNum,pageSize,status,userId);
        return PageResponse.of(pages,ReservationVo.class);
    }

    /**
     * 更新预约
     */
    @Override
    public void update(Long id, ReservationDto dto) {
        Reservation reservation = reservationMapper.findById(id);
        if (reservation != null) {
            BeanUtils.copyProperties(dto, reservation);
            reservation.setId(id);
            reservation.setUpdateTime(LocalDateTime.now());
            reservationMapper.update(reservation);
        }
    }

    /**
     * 取消预约
     */
    @Override
    public void cancelReservation(Long id) {
        Reservation reservation = reservationMapper.findById(id);
        if (reservation != null) {
            reservation.setStatus(ReservationStatus.CANCELED.getOrdinal());
            reservationMapper.update(reservation);
        }
    }

    /**
     * 根据id删除预约
     */
    @Override
    public void deleteById(Long id) {
        reservationMapper.deleteById(id);
    }

    /**
     * 根据id查找预约
     */
    @Override
    public ReservationVo findById(Long id) {
        Reservation reservation = reservationMapper.findById(id);
        if (reservation != null) {
            return convertToVO(reservation);
        }
        return null;
    }


    /**
     * 查找所有预约
     * @param mobile 预约人手机号
     * @param time 预约时间
     */
    @Override
    public List<ReservationVo> findAll(String mobile, LocalDateTime time) {
        LocalDateTime endTime = time.plusHours(24); // 计算24小时后的时间
        Long userId = UserThreadLocal.getUserId();
        List<Reservation> reservations = reservationMapper.findAll(userId, mobile, time, endTime); // 根据mobile和时间范围查询预约
        return convertToVOList(reservations);
    }



    /**
     * 分页查找预约
     * @param page 页码
     * @param size 每页大小
     * @param name 预约人姓名
     * @param phone 预约人手机号
     * @param status 预约状态
     * @param type 预约类型
     * @param startTime
     * @param endTime
     * @return 预约列表
     */
    @Override
    public PageResponse<ReservationVo> findByPage(int page, int size, String name, String phone, Integer status, Integer type, LocalDateTime startTime, LocalDateTime endTime) {
        PageHelper.startPage(page, size);
        Long userId = UserThreadLocal.getUserId();
        Page<Reservation> byPage = reservationMapper.findByPage(page, size, name, phone, status, type, userId, startTime, endTime);
        return PageResponse.of(byPage, ReservationVo.class);
    }


    /**
     * 将Reservation转换为ReservationVO
     */
    private ReservationVo convertToVO(Reservation reservation) {
        return BeanUtil.toBean(reservation, ReservationVo.class);
    }

    /**
     * 将List<Reservation>转换为List<ReservationVO>
     */
    private List<ReservationVo> convertToVOList(List<Reservation> reservations) {
        return reservations.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    /**
     * 将时间戳转换为字符串
     */
    private String convertTimeToStr(Long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
    }

    /**
     * 如果预约未完成，则将预约状态更新为过期
     */
    @Override
    public void updateVisitReservationStatusToExpiredIfNotCompleted(Long id) {
        Reservation visitReservation = reservationMapper.findById(id);
        if (visitReservation.getStatus().equals(ReservationStatus.COMPLETED.getOrdinal())
                || visitReservation.getStatus().equals(ReservationStatus.CANCELED.getOrdinal()) ) {
            return;
        }
        LocalDateTime reservationTime = visitReservation.getTime();
        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isAfter(reservationTime.plusHours(1))) {
            visitReservation.setStatus(ReservationStatus.EXPIRED.getOrdinal());
            reservationMapper.update(visitReservation);
        }
    }



    /**
     * 来访
     * @param id ID
     * @param time 时间
     */
    @Override
    public void visit(Long id, Long time) {
        Reservation reservation = reservationMapper.findById(id);
        if (reservation != null) {
            reservation.setStatus(ReservationStatus.COMPLETED.getOrdinal());
            reservationMapper.update(reservation);
            VisitDto visitDto = BeanUtil.toBean(reservation, VisitDto.class);
            visitDto.setTime(LocalDateTimeUtil.of(time));
            visitService.add(visitDto);
        }
    }

    @Override
    public void updateReservationStatus(LocalDateTime now) {

        reservationMapper.updateReservationStatus(now);
    }
}

