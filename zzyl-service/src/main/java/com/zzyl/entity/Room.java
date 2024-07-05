package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Room extends BaseEntity {

    /**
     * 房间编号
     */
    @ApiModelProperty(value = "房间编号")
    private String code;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    private Integer sort;

    /**
     * 房间类型名称
     */
    @ApiModelProperty(value = "房间类型名称")
    private String typeName;

    /**
     * 楼层id
     */
    @ApiModelProperty(value = "楼层id")
    private Long floorId;

}
