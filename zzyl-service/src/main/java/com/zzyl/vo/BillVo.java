package com.zzyl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseVo;
import com.zzyl.vo.retreat.ElderVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description = "账单信息")
public class BillVo extends BaseVo {

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "账单类型（0：月度账单，1服务账单）")
    private Integer billType;

    @ApiModelProperty(value = "账单月份")
    private String billMonth;

    @ApiModelProperty(value = "老人ID")
    private Long elderId;

    @ApiModelProperty(value = "账单金额")
    private BigDecimal billAmount;

    @ApiModelProperty(value = "其他支付方式的金额")
    private BigDecimal otherAmount;

    @ApiModelProperty(value = "应付金额")
    private BigDecimal payableAmount;

    @ApiModelProperty(value = "缴费截止日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDeadline;

    @ApiModelProperty(value = "交易状态 账单状态（0：未支付，1已支付, 2已关闭）")
    private Integer transactionStatus;

    @ApiModelProperty(value = "账单开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime billStartTime;

    @ApiModelProperty(value = "账单结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime billEndTime;

    @ApiModelProperty(value = "账单总天数")
    private Integer totalDays;

    /**
     * 本期应付
     */
    @ApiModelProperty(value = "本期应付")
    private BigDecimal currentCost;

    /**
     * 押金金额
     */
    @ApiModelProperty(value = "押金金额")
    private BigDecimal depositAmount;

    /**
     * 预付款支付金额
     */
    @ApiModelProperty(value = "预付款支付金额")
    private BigDecimal prepaidAmount;

    @ApiModelProperty(value = "等级名称")
    private String lname;


    @ApiModelProperty(value = "房间类型名称")
    private String typeName;

    @ApiModelProperty(value = "老人")
    private ElderVo elderVo;

    @ApiModelProperty(value = "入住配置")
    private CheckInConfigVo checkInConfigVo;


    @ApiModelProperty(value = "交易号")
    private Long tradingOrderNo;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "支付记录")
    private List<TradingVo> tradingVo;

    @ApiModelProperty(value = "退款记录")
    private RefundRecordVo refundRecordVo;

    @ApiModelProperty(value = "总欠费金额")
    private BigDecimal total;

    @ApiModelProperty(value = "床位")
    private BedVo bedVo;

    private String memberCreator;

}
