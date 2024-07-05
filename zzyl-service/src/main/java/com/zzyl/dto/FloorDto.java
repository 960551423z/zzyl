package com.zzyl.dto;

import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FloorDto extends BaseDto {

    /**
     * 楼层名称
     */
    @ApiModelProperty(value = "楼层名称")
    private String name;

    /**
     * 楼层编号
     */
    @ApiModelProperty(value = "楼层编号")
    private Integer code;
}
