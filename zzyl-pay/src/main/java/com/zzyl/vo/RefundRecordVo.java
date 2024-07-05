package com.zzyl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="RefundRecordVo对象", description="")
public class RefundRecordVo extends BaseVo {

    private static final long serialVersionUID = 1L;

    @Builder
    public RefundRecordVo(Long id, String dataState, Long tradingOrderNo, Long productOrderNo, Long refundNo, Long enterpriseId, Long storeId, String tradingChannel, Integer refundStatus, String refundCode, String refundMsg, String memo, BigDecimal refundAmount,Integer orderStatus) {
        super(id, dataState);
        this.tradingOrderNo = tradingOrderNo;
        this.productOrderNo = productOrderNo;
        this.refundNo = refundNo;
        this.enterpriseId = enterpriseId;
        this.storeId = storeId;
        this.tradingChannel = tradingChannel;
        this.refundStatus = refundStatus;
        this.refundCode = refundCode;
        this.refundMsg = refundMsg;
        this.memo = memo;
        this.refundAmount = refundAmount;
        this.orderStatus = orderStatus;
    }

    @ApiModelProperty(value = "交易系统订单号【对于三方来说：商户订单】")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tradingOrderNo;

    @ApiModelProperty(value = "业务系统订单号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productOrderNo;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "本次退款订单号")
    private Long refundNo;

    @ApiModelProperty(value = "本次退款订单号 字符串")
    private String refundNoStr;

    @ApiModelProperty(value = "商户号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long enterpriseId;

    @ApiModelProperty(value = "门店主键id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long storeId;

    @ApiModelProperty(value = "退款渠道【支付宝、微信、现金】")
    private String tradingChannel;

    @ApiModelProperty(value = "退款状态【成功：2,进行中：1, 失败：3】")
    private Integer refundStatus;

    @ApiModelProperty(value = "返回编码")
    private String refundCode;

    @ApiModelProperty(value = "返回信息")
    private String refundMsg;

    @ApiModelProperty(value = "备注【订单门店，桌台信息】")
    private String memo;

    @ApiModelProperty(value = "本次退款金额")
    private BigDecimal refundAmount;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Integer orderStatus;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "页码")
    private int pageNum = 1;
    @ApiModelProperty(value = "页大小")
    private int pageSize = 10;


    @ApiModelProperty(value = "订单编码")
    private String orderNo;

}
