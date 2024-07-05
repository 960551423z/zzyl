package com.zzyl.enums;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "合同状态枚举")
public enum ContractStatusEnum {

    @ApiModelProperty(value = "未生效")
    PENDING_EFFECTIVE(0, "未生效"),
    @ApiModelProperty(value = "生效中")
    EFFECTIVE(1, "生效中"),
    @ApiModelProperty(value = "已过期")
    EXPIRED(2, "已过期"),
    @ApiModelProperty(value = "已失效")
    UN_EFFECTIVE(3, "已失效");

    private Integer ordinal;
    private String name;

    ContractStatusEnum(Integer ordinal, String name) {
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
