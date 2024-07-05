package com.zzyl.dto;

import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "房型信息")
public class RoomTypeDto extends BaseDto {

    /**
     * 房型名称
     */
    @ApiModelProperty(value = "房型名称", example = "豪华大床房")
    private String name;

    /**
     * 床位数量
     */
    @ApiModelProperty(value = "床位数量", example = "2")
    private Integer bedCount;

    /**
     * 床位费用
     */
    @ApiModelProperty(value = "床位费用", example = "399.99")
    private BigDecimal price;

    /**
     * 介绍
     */
    @ApiModelProperty(value = "介绍", example = "简短的房型介绍")
    private String introduction;

    /**
     * 照片
     */
    @ApiModelProperty(value = "照片", example = "http://image.com/roomtype.jpg")
    private String photo;

    /**
     * 类型名称
     */
    @ApiModelProperty(value = "类型名称", example = "豪华型")
    private String typeName;

    /**
     * 状态，0：禁用，1：启用
     */
    @ApiModelProperty(value = "状态，0：禁用，1：启用", example = "1")
    private Integer status;
}
