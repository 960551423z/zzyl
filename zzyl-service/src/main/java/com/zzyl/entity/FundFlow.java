package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 资金流水实体类
 */
@Data
public class FundFlow extends BaseEntity {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 余额类型
     */
    private Integer balanceType;

    /**
     * 资金流向
     */
    private Integer fundDirection;

    /**
     * 相关单据号
     */
    private String relatedBillNo;

    /**
     * 流水原因
     */
    private String flowReason;

    /**
     * 金额
     */
    private BigDecimal amount;

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

