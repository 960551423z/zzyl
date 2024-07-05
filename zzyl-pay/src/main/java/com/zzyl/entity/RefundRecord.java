package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;

/**
 * 退款记录表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RefundRecord extends BaseEntity {

    private static final long serialVersionUID = -3998253241655800061L;

    @ApiModelProperty(value = "交易系统订单号【对于三方来说：商户订单】")
    private Long tradingOrderNo;

    @ApiModelProperty(value = "业务系统订单号")
    private Long productOrderNo;

    @ApiModelProperty(value = "本次退款订单号")
    private Long refundNo;

    @ApiModelProperty(value = "商户号")
    private Long enterpriseId;

    @ApiModelProperty(value = "退款渠道【支付宝、微信、现金】")
    private String tradingChannel;

    @ApiModelProperty(value = "退款状态")
    private Integer refundStatus;

    @ApiModelProperty(value = "返回编码")
    private String refundCode;

    @ApiModelProperty(value = "返回信息")
    private String refundMsg;

    @ApiModelProperty(value = "备注【订单门店，桌台信息】")
    private String memo;

    @ApiModelProperty(value = "本次退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "原订单金额")
    private BigDecimal total;

    private String dataState;

    private Integer createType;

    @Builder
    public RefundRecord(Long id, String dataState, Long tradingOrderNo, Long productOrderNo, Long refundNo, Long enterpriseId, String tradingChannel, Integer refundStatus, String refundCode, String refundMsg, String memo, BigDecimal refundAmount, BigDecimal total) {
        super(id);
        this.tradingOrderNo = tradingOrderNo;
        this.productOrderNo = productOrderNo;
        this.refundNo = refundNo;
        this.enterpriseId = enterpriseId;
        this.tradingChannel = tradingChannel;
        this.refundStatus = refundStatus;
        this.refundCode = refundCode;
        this.refundMsg = refundMsg;
        this.memo = memo;
        this.refundAmount = refundAmount;
        this.total = total;
        this.dataState = dataState;
    }
}
