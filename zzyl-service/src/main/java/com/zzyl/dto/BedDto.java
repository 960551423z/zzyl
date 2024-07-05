package com.zzyl.dto;

import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("床位实体类")
public class BedDto extends BaseDto {
    /**
     * 床位编号
     */
    @ApiModelProperty("床位编号")
    private String bedNumber;

    /**
     * 床位状态: 未入住0, 已入住1
     */
    @ApiModelProperty(value = "床位状态: 未入住0, 已入住1",example = "0")
    private Integer bedStatus;

    /**
     * 房间ID
     */
    @ApiModelProperty("房间ID")
    private Long roomId;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    private Integer sort;
}
