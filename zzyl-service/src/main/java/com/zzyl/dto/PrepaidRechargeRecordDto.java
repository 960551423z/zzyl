package com.zzyl.dto;

import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "预付费充值记录")
public class PrepaidRechargeRecordDto extends BaseDto {

    @ApiModelProperty(value = "充值金额", example = "100.00")
    private BigDecimal rechargeAmount;

    @ApiModelProperty(value = "充值凭证")
    private String rechargeVoucher;

    @ApiModelProperty(value = "充值方式")
    private String rechargeMethod;

    @ApiModelProperty(value = "老人ID", example = "1")
    private Long elderId;

    @ApiModelProperty(value = "老人姓名")
    private String elderName;

    @ApiModelProperty(value = "床位号")
    private String bedNo;
}
