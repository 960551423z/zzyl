package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomType extends BaseEntity {

    /**
     * 房型名称
     */
    @ApiModelProperty("房型名称")
    private String name;

    /**
     * 床位数量
     */
    @ApiModelProperty("床位数量")
    private Integer bedCount;

    /**
     * 床位费用
     */
    @ApiModelProperty("床位费用")
    private BigDecimal price;

    /**
     * 介绍
     */
    @ApiModelProperty("介绍")
    private String introduction;

    /**
     * 照片
     */
    @ApiModelProperty("照片")
    private String photo;

    /**
     * 类型名称
     */
    @ApiModelProperty("类型名称")
    private String typeName;

    /**
     * 状态，0：禁用，1：启用
     */
    @ApiModelProperty("状态，0：禁用，1：启用")
    private Integer status;
}
