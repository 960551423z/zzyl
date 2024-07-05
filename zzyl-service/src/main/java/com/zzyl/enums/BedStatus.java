package com.zzyl.enums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 阿庆
 */
@ApiModel(description = "床位状态枚举")
public enum BedStatus {
    @ApiModelProperty(value = "未入住")
    UNOCCUPIED(0, "未入住"),
    @ApiModelProperty(value = "已入住")
    OCCUPIED(1, "已入住");

    private Integer ordinal;
    private String name;

    BedStatus(Integer ordinal, String name) {
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

