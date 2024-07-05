package com.zzyl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName TradingVo.java
 *  交易结果
 */
@Data
@NoArgsConstructor
public class TradingVo extends BaseVo {

    private static final long serialVersionUID = 1L;

    @Builder
    public TradingVo(Long id, String dataState, String openId, Long productOrderNo, Long tradingOrderNo, String tradingChannel, Integer tradingState, String payeeName, Long payeeId, String payerName, Long payerId, BigDecimal tradingAmount, BigDecimal refund, String isRefund, String resultCode, String resultMsg, String resultJson, String placeOrderCode, String placeOrderMsg, String placeOrderJson, Long enterpriseId, Long storeId, String memo, String qrCodeImageBase64, Long outRequestNo, BigDecimal operTionRefund, String authCode, String quitUrl, String returnUrl, String billType, Date billDate, String billDownloadUrl) {
        super(id, dataState);
        this.openId = openId;
        this.productOrderNo = productOrderNo;
        this.tradingOrderNo = tradingOrderNo;
        this.tradingChannel = tradingChannel;
        this.tradingState = tradingState;
        this.payeeName = payeeName;
        this.payeeId = payeeId;
        this.payerName = payerName;
        this.payerId = payerId;
        this.tradingAmount = tradingAmount;
        this.refund = refund;
        this.isRefund = isRefund;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.resultJson = resultJson;
        this.placeOrderCode = placeOrderCode;
        this.placeOrderMsg = placeOrderMsg;
        this.placeOrderJson = placeOrderJson;
        this.enterpriseId = enterpriseId;
        this.storeId = storeId;
        this.memo = memo;
        this.qrCodeImageBase64 = qrCodeImageBase64;
        this.outRequestNo = outRequestNo;
        this.operTionRefund = operTionRefund;
        this.authCode = authCode;
        this.quitUrl = quitUrl;
        this.returnUrl = returnUrl;
        this.billType = billType;
        this.billDate = billDate;
        this.billDownloadUrl = billDownloadUrl;
    }

    @ApiModelProperty(value = "openId标识")
    private String openId;

    @ApiModelProperty(value = "业务系统订单号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productOrderNo;

    @ApiModelProperty(value = "交易系统订单号【对于三方来说：商户订单】")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tradingOrderNo;

    @ApiModelProperty(value = "支付渠道【支付宝、微信】")
    private String tradingChannel;

    @ApiModelProperty(value = "交易单状态")
    private Integer tradingState;

    @ApiModelProperty(value = "收款人姓名")
    private String payeeName;

    @ApiModelProperty(value = "交易类型【1账单，2订单】")
    private String tradingType;

    @ApiModelProperty(value = "收款人账户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long payeeId;

    @ApiModelProperty(value = "付款人姓名")
    private String payerName;

    @ApiModelProperty(value = "付款人Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long payerId;

    @ApiModelProperty(value = "交易金额")
    private BigDecimal tradingAmount;

    @ApiModelProperty(value = "退款总金额")
    private BigDecimal refund;

    @ApiModelProperty(value = "是否有退款：YES，NO")
    private String isRefund;

    @ApiModelProperty(value = "第三方交易返回编码【最终确认交易结果】")
    private String resultCode;

    @ApiModelProperty(value = "第三方交易返回提示消息【最终确认交易信息】")
    private String resultMsg;

    @ApiModelProperty(value = "第三方交易返回信息json【分析交易最终信息】")
    private String resultJson;

    @ApiModelProperty(value = "统一下单返回编码")
    private String placeOrderCode;

    @ApiModelProperty(value = "统一下单返回信息")
    private String placeOrderMsg;

    @ApiModelProperty(value = "统一下单返回信息json【用于生产二维码、Android ios唤醒支付等】")
    private String placeOrderJson;

    @ApiModelProperty(value = "商户号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long enterpriseId;

    @ApiModelProperty(value = "门店主键id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long storeId;

    @ApiModelProperty(value = "线下支付凭证【支付凭证链接】")
    private String memo;

    @ApiModelProperty(value = "二维码base64")
    private String qrCodeImageBase64;

    @ApiModelProperty(value = "退款请求号")
    private Long outRequestNo;

    @ApiModelProperty(value = "本次退款金额")
    private BigDecimal operTionRefund;

    @ApiModelProperty(value = "支付授权码")
    private String authCode;

    @ApiModelProperty(value = "支付宝：HTTP/HTTPS开头字符串")
    private String quitUrl;

    @ApiModelProperty(value = "支付宝：用户付款中途退出返回商户网站的地址")
    private String returnUrl;

    @ApiModelProperty(value = "账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型")
    private String billType;

    @ApiModelProperty(value = "账单时间:日账单格式为yyyy-MM-dd，最早可下载2016年1月1日开始的日账单。不支持下载当日账单，只能下载前一日24点前的账单数据（T+1）")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")//get
    protected Date billDate;

    @ApiModelProperty(value = "账单地址")
    private String billDownloadUrl;

    String phone;
}
