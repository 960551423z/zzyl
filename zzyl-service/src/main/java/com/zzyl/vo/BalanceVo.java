package com.zzyl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "BalanceDto", description = "余额信息")
public class BalanceVo extends BaseVo {

    @ApiModelProperty(value = "预付款余额")
    private BigDecimal prepaidBalance;

    @ApiModelProperty(value = "押金金额")
    private BigDecimal depositAmount;

    @ApiModelProperty(value = "欠费金额")
    private BigDecimal arrearsAmount;

    @ApiModelProperty(value = "缴费截止日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDeadline;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "老人ID")
    private Long elderId;

    @ApiModelProperty(value = "老人姓名")
    private String elderName;

    @ApiModelProperty(value = "床位号")
    private String bedNo;
    /**
     * 扣款备注
     */
    private String description;

}
