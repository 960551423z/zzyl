package com.zzyl.vo.retreat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 欠款 月度欠费
 * @author 阿庆
 */
@Data
@ApiModel(description = "欠款(月度欠费)")
public class Arrearage {

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
     * 账单月份
     */
    @ApiModelProperty(value = "账单月份")
    private String billMonth;

    /**
     * 可退金额
     */
    @ApiModelProperty(value = "可退金额")
    private BigDecimal amount;
}
