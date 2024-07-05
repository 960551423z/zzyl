package com.zzyl.dto;

import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "NursingLevelDto对象", description = "护理等级DTO类")
public class NursingLevelDto extends BaseDto {

    /**
     * 等级名称
     */
    @ApiModelProperty(value = "等级名称")
    private String name;

    /**
     * 护理计划名称
     */
    @ApiModelProperty(value = "护理计划名称")
    private String planName;

    /**
     * 护理计划ID
     */
    @ApiModelProperty(value = "护理计划ID")
    private Long planId;

    /**
     * 护理费用
     */
    @ApiModelProperty(value = "护理费用")
    private BigDecimal fee;

    /**
     * 状态（0：禁用，1：启用）
     */
    @ApiModelProperty(value = "状态（0：禁用，1：启用）")
    private Integer status;

    /**
     * 等级说明
     */
    @ApiModelProperty(value = "等级说明")
    private String description;
}

