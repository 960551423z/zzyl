package com.zzyl.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseEntity;
import com.zzyl.vo.*;
import com.zzyl.vo.retreat.ElderVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order extends BaseEntity {

    /**
     * 订单号
     */
    private Long tradingOrderNo;

    /**
     * 支付状态
     */
    private Integer paymentStatus;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 退款金额
     */
    private BigDecimal refund;

    /**
     * 是否退款
     */
    private String isRefund;

    /**
     * 下单会员ID
     */
    private Long memberId;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 老人ID
     */
    private Long elderId;

    /**
     * 预计到达时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedArrivalTime;

    /**
     * 备注
     */
    private String orderNo;

    /**
     * 原因
     */
    private String reason;

    /**
     * 订单状态
     */
    private Integer status;

    private BedVo bedVo;
    private ElderVo elderVo;
    private MemberVo memberVo;
    private NursingProjectVo nursingProjectVo;

    @ApiModelProperty(value = "支付信息")
    private TradingVo tradingVo;

    @ApiModelProperty(value = "退款记录")
    private RefundRecordVo refundRecordVo;

    @ApiModelProperty(value = "执行记录")
    private NursingTaskVo nursingTaskVo;

    private Integer createType;
}
