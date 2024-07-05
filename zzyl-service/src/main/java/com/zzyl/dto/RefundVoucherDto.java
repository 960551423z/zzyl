package com.zzyl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 阿庆
 */
@Data
@ApiModel("退款凭证DTO")
public class RefundVoucherDto {

    /**
     * 退款渠道【支付宝、微信、现金】
     */
    @ApiModelProperty(value = "退款渠道【支付宝、微信、现金】")
    private String tradingChannel;

    /**
     * 退款凭证URL
     */
    @ApiModelProperty(value = "退款凭证URL")
    private String refundVoucherUrl;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 退住单编码
     */
    @ApiModelProperty(value = "退住单编码")
    private String retreatCode;

    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;

}
