package com.zzyl.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 入住配置实体类
 */
@Data
public class CheckInConfig extends BaseEntity  {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 老人id
     */
    private Long elderId;

    /**
     * 入住开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkInStartTime;

    /**
     * 入住结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkInEndTime;

    /**
     * 护理等级id
     */
    private Long nursingLevelId;

    /**
     * 床位号
     */
    private String bedNo;

    /**
     * 费用开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime costStartTime;

    /**
     * 费用结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime costEndTime;

    /**
     * 押金金额
     */
    private BigDecimal depositAmount;

    /**
     * 护理费用
     */
    private BigDecimal nursingCost;

    /**
     * 床位费用
     */
    private BigDecimal bedCost;

    /**
     * 其他费用
     */
    private BigDecimal otherCost;

    /**
     * 医保支付金额
     */
    private BigDecimal medicalInsurancePayment;

    /**
     * 政府补贴金额
     */
    private BigDecimal governmentSubsidy;

    /**
     * 护理等级
     */
    private NursingLevel nursingLevel;
}
