package com.zzyl.enums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 阿庆
 */
@ApiModel(description = "账单状态枚举")
public enum BillStatus {
    @ApiModelProperty(value = "未支付")
    UN_PAY(0, "未支付"),
    @ApiModelProperty(value = "已支付")
    PAY(1, "已支付"),
    @ApiModelProperty(value = "已关闭")
    CLOSE(2, "已关闭");

    private Integer ordinal;
    private String name;

    BillStatus(Integer ordinal, String name) {
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

