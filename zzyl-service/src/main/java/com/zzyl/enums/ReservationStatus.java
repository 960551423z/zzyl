package com.zzyl.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;

/**
 * 预约状态
 */
@ApiModel(description = "预约状态")
public enum ReservationStatus {
    PENDING("待报道", 0), //待报道
    COMPLETED("已完成", 1), //已完成
    CANCELED("取消", 2), //取消
    EXPIRED("过期", 3); //过期

    private String name;
    @JsonValue
    private Integer ordinal;

    ReservationStatus(String name, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    public String getName() {
        return name;
    }

    public int getOrdinal() {
        return ordinal;
    }
}

