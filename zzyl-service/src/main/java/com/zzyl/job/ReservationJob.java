package com.zzyl.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import com.zzyl.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author 阿庆
 */
@Component
@Slf4j
public class ReservationJob {

    @Autowired
    private ReservationService reservationService;

    @XxlJob("reservationStatusToExpired")
    public void updateReservationStatus() {
        log.info("预约状态-过期修改-begin");
        reservationService.updateReservationStatus(LocalDateTime.now());
        log.info("预约状态-过期修改-end");
    }
}
