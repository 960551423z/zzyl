package com.zzyl.enums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 阿庆
 */
@ApiModel(description = "账单类型枚举")
public enum BillType {
    @ApiModelProperty(value = "月度账单")
    MONTH(0, "月度账单"),
    @ApiModelProperty(value = "服务账单")
    PROJECT(1, "服务账单");

    private Integer ordinal;
    private String name;

    BillType(Integer ordinal, String name) {
        this.ordinal = ordinal;
        this.name = name;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public String getName() {
        return name;
    }
}

