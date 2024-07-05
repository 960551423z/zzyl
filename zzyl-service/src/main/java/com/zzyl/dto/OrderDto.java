package com.zzyl.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "订单信息")
public class OrderDto extends BaseDto {

    @ApiModelProperty(value = "支付状态")
    private Byte paymentStatus;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "老人ID")
    private Long elderId;

    @ApiModelProperty(value = "预计到达时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedArrivalTime;

}

