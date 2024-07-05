package com.zzyl.job;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zzyl.dto.BillDto;
import com.zzyl.service.BillService;
import com.zzyl.service.ElderService;
import com.zzyl.service.NursingTaskService;
import com.zzyl.vo.retreat.ElderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 账单管理
 * @author 阿庆
 */
@Slf4j
@Component
public class BillJob {

    @Resource
    BillService billService;

    @Resource
    ElderService elderService;

    @Resource
    private NursingTaskService nursingTaskService;

    /**
     * 生成月度账单和护理任务
     */
    @XxlJob("createMothBillAndTaskJob")
    public void createMothBillAndTaskJob() {
        List<ElderVo> elderVos = elderService.selectList();
        String format = LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy-MM");
        for (ElderVo elder : elderVos) {
            BillDto billDto = new BillDto();
            billDto.setElderId(elder.getId());
            billDto.setBillMonth(format);
            try {
                billService.createMonthBill(billDto);
                nursingTaskService.createMonthTask(elder, LocalDateTime.now(), LocalDateTime.now());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
