package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 充值记录实体类
 */
@Data
public class PrepaidRechargeRecord extends BaseEntity {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 充值金额
     */
    private BigDecimal rechargeAmount;

    /**
     * 充值凭证
     */
    private String rechargeVoucher;

    /**
     * 充值方式
     */
    private String rechargeMethod;

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

    /**
     * 编号
     */
    private String prepaidRechargeNo;

}
