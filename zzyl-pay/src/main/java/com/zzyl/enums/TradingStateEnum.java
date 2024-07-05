package com.zzyl.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 交易单状态枚举
 *
 * @author 阿庆
 * @version 1.0
 */
public enum TradingStateEnum {

    DFK(1, "待付款"),
    FKZ(2, "付款中"),
    FKSB(3, "付款失败"),
    YJS(4, "已结算（已付款）"),
    QXDD(5, "取消订单"),
    MD(6, "免单"),
    GZ(7, "挂账");

    @JsonValue
    private Integer code;
    private String value;

    TradingStateEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }


    public Integer getCode() {
        return this.code;
    }


    public String getValue() {
        return this.value;
    }
}
