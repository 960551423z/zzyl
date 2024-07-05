package com.zzyl.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseEntity;
import com.zzyl.vo.BedVo;
import com.zzyl.vo.CheckInConfigVo;
import com.zzyl.vo.retreat.ElderVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 账单实体类
 */
@Data
public class Bill extends BaseEntity {
    /**
     * 账单id
     */
    private Long id;

    /**
     * 账单编号
     */
    private String billNo;

    /**
     * 账单类型
     */
    private Integer billType;

    /**
     * 账单月份
     */
    private String billMonth;

    /**
     * 老人id
     */
    private Long elderId;

    /**
     * 账单金额
     */
    private BigDecimal billAmount;

    /**
     * 应付金额
     */
    private BigDecimal payableAmount;

    /**
     * 缴费截止日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDeadline;

    /**
     * 交易状态
     */
    private Integer transactionStatus;

    /**
     * 账单开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime billStartTime;

    /**
     * 账单结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime billEndTime;

    /**
     * 账单总天数
     */
    private Integer totalDays;

    /**
     * 本期应付
     */
    private BigDecimal currentCost;

    /**
     * 押金金额
     */
    private BigDecimal depositAmount;

    /**
     * 预付款支付金额
     */
    private BigDecimal prepaidAmount;

    private Long tradingOrderNo;

    @ApiModelProperty(value = "等级名称")
    private String lname;

    @ApiModelProperty(value = "房间类型名称")
    private String typeName;

    private ElderVo elderVo;

    @ApiModelProperty(value = "总欠费金额")
    private BigDecimal total;

    private CheckInConfigVo checkInConfigVo;

    private BedVo bedVo;
}
