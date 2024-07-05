package com.zzyl.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 *  数据权限对象
 */
@Data
@NoArgsConstructor
public class DataSecurity implements Serializable {

    @ApiModelProperty(value = "仅本人数据权限")
    private Boolean youselfData;

    @ApiModelProperty(value = "数据权限对应部门编号集合")
    private List<String> deptNos;

    @Builder
    public DataSecurity(Boolean youselfData, List<String> deptNos) {
        this.youselfData = youselfData;
        this.deptNos = deptNos;
    }
}
