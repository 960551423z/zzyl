package com.zzyl.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 余额实体类
 */
@Data
public class Balance extends BaseEntity {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 预付款余额
     */
    private BigDecimal prepaidBalance;

    /**
     * 押金金额
     */
    private BigDecimal depositAmount;

    /**
     * 欠费金额
     */
    private BigDecimal arrearsAmount;

    /**
     * 缴费截止日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDeadline;

    /**
     * 状态（0：正常，1：退住）
     */
    private Integer status;

    /**
     * 老人id
     */
    private Long elderId;

    /**
     * 老人姓名
     */
    private String elderName;

    /**
     * 床位号
     */
    private String bedNo;
}
