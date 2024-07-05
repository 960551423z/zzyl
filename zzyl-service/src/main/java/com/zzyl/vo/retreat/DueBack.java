package com.zzyl.vo.retreat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 费用应退  （月度账单+微服务的订单）
 * @author 阿庆
 */
@Data
@ApiModel(description = "费用应退(月度账单+微服务的订单)")
public class DueBack {

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

    /**
     * 护理项目
     */
    @ApiModelProperty(value = "护理项目")
    private String nursingName;

    /**
     * 实退金额
     */
    @ApiModelProperty(value = "实退金额")
    private BigDecimal realAmount;

    /**
     * 调整备注
     */
    @ApiModelProperty(value = "调整备注")
    private String remark;


    @ApiModelProperty(value = "订单号")
    private Long tradingOrderNo;

    /**
     * 退住天数
     */
    private Integer surplusDay;

    /**
     * 退住天数
     */
    private Integer realDay;

    /**
     * 退住天数
     */
    private Integer dueBackDay;
}
