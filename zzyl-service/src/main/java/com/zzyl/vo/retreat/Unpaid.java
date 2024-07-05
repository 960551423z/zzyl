package com.zzyl.vo.retreat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 未缴（订单）
 * @author 阿庆
 */
@Data
@ApiModel(description = "未缴（订单）")
public class Unpaid {

    /**
     * 账单编码
     */
    @ApiModelProperty(value = "账单编码")
    private String code;
    /**
     * 账单类型
     * 0:月度账单
     * 1:订单
     */
    @ApiModelProperty(value = "账单类型(0:月度账单,1:订单)")
    private int type;

    /**
     * 可退金额
     */
    @ApiModelProperty(value = "可退金额")
    private BigDecimal amount;

    /**
     * 护理项目
     */
    @ApiModelProperty(value = "护理项目")
    private String nursingName;

}
