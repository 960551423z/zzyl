package com.zzyl.vo;

import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "预付费充值记录")
public class PrepaidRechargeRecordVo extends BaseVo {

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

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private String prepaidRechargeNo;
}
