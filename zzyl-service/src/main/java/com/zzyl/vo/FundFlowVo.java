package com.zzyl.vo;

import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "资金流水信息")
public class FundFlowVo extends BaseVo {

    @ApiModelProperty(value = "余额类型")
    private Integer balanceType;

    @ApiModelProperty(value = "资金流向")
    private Integer fundDirection;

    @ApiModelProperty(value = "关联单据号")
    private String relatedBillNo;

    @ApiModelProperty(value = "资金流水原因")
    private String flowReason;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "老人ID")
    private Long elderId;

    @ApiModelProperty(value = "老人姓名")
    private String elderName;

    @ApiModelProperty(value = "床位号")
    private String bedNo;

}
