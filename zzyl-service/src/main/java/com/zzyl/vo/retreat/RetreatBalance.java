package com.zzyl.vo.retreat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 余额（押金+预缴款）
 * @author 阿庆
 */
@Data
@ApiModel(description = "余额(押金+预缴款)")
public class RetreatBalance {

    /**
     * 押金
     */
    @ApiModelProperty(value = "押金")
    private BigDecimal depositAmount;

    /**
     * 预缴款
     */
    @ApiModelProperty(value = "预缴款")
    private BigDecimal prepaidAmount;
}
