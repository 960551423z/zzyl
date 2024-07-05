package com.zzyl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseVo;
import com.zzyl.vo.retreat.ElderVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "订单信息")
public class OrderVo extends BaseVo {

    @ApiModelProperty(value = "交易号")
    private Long tradingOrderNo;

    @ApiModelProperty(value = "支付状态 1未付 2已付 3已关闭")
    private Integer paymentStatus;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal refund;

    @ApiModelProperty(value = "是否退款")
    private String isRefund;

    @ApiModelProperty(value = "下单会员ID")
    private Long memberId;

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "老人ID")
    private Long elderId;

    @ApiModelProperty(value = "预计到达时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedArrivalTime;

    @ApiModelProperty(value = "订单编码")
    private String orderNo;

    @ApiModelProperty(value = "原因")
    private String reason;

    @ApiModelProperty(value = "订单状态 0待支付 1待执行 2已执行 3已完成 4已关闭 5已退款")
    private Integer status;

    @ApiModelProperty(value = "老人信息")
    private ElderVo elderVo;

    @ApiModelProperty(value = "下单人信息")
    private MemberVo memberVo;

    @ApiModelProperty(value = "护理项目")
    private com.zzyl.vo.NursingProjectVo nursingProjectVo;

    @ApiModelProperty(value = "床位")
    private com.zzyl.vo.BedVo bedVo;

    @ApiModelProperty(value = "支付信息")
    private TradingVo tradingVo;

    @ApiModelProperty(value = "退款记录")
    private RefundRecordVo refundRecordVo;

    @ApiModelProperty(value = "执行记录")
    private NursingTaskVo nursingTaskVo;
}
