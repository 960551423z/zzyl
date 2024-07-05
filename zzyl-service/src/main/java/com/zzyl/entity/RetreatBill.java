package com.zzyl.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
* @author 阿庆
*/
@Data
@ApiModel(description = "退住账单实体类")
public class RetreatBill implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    private Long id;

    /**
     * 账单json数据
     */
    private String billJson;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 老人ID
     */
    private Long elderId;

    /**
     * 是否退款
     */
    private Integer isRefund;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款凭证URL
     */
    private String refundVoucherUrl;

    /**
     * 备注
     */
    private String remark;

    /**
     * 退住id
     */
    private Long retreatId;

    /**
     * 支付渠道
     */
    private String tradingChannel;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
