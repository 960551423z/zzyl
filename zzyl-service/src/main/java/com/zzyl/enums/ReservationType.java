package com.zzyl.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;


@ApiModel(description = "预约类型")
public enum ReservationType {

    VISIT("参观预约", 0),
    EXPLORE("探访预约", 1);

    private String name;
    @JsonValue
    private Integer ordinal;

    ReservationType(String name, int ordinal) {
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


