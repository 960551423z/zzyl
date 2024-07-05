package com.zzyl.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "账单信息")
public class BillDto extends BaseDto {

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "账单类型")
    private Integer billType;

    @ApiModelProperty(value = "账单月份 yyyy-mm-dd")
    private String billMonth;

    @ApiModelProperty(value = "老人ID")
    private Long elderId;

    @ApiModelProperty(value = "账单金额")
    private BigDecimal billAmount;

    @ApiModelProperty(value = "应付金额")
    private BigDecimal payableAmount;

    @ApiModelProperty(value = "缴费截止日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDeadline;

    @ApiModelProperty(value = "账单状态（0：未支付，1已支付, 2已关闭）")
    private Integer transactionStatus;

    @ApiModelProperty(value = "账单开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime billStartTime;

    @ApiModelProperty(value = "账单结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime billEndTime;

    @ApiModelProperty(value = "账单总天数")
    private Integer totalDays;

    @ApiModelProperty(value = "交易号")
    private Long tradingOrderNo;

    @ApiModelProperty(value = "等级名称")
    private String lname;


    @ApiModelProperty(value = "房间类型名称")
    private String typeName;

}
