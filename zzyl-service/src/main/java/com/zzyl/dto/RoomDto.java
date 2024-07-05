package com.zzyl.dto;

import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "RoomDto", description = "客房信息")
public class RoomDto extends BaseDto {

    /**
     * 客房编号
     */
    @ApiModelProperty(value = "客房编号")
    private String code;

    /**
     * 客房排序
     */
    @ApiModelProperty(value = "客房排序")
    private Integer sort;

    /**
     * 客房类型名称
     */
    @ApiModelProperty(value = "客房类型名称")
    private String typeName;

    /**
     * 楼层id
     */
    @ApiModelProperty(value = "楼层id")
    private Long floorId;
}
