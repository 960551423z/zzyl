package com.zzyl.dto;

import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class NursingPlanDto extends BaseDto {

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    private Integer sortNo;

    /**
     * 计划名称
     */
    @ApiModelProperty(value = "计划名称")
    private String planName;

    /**
     * 状态（0：禁用，1：启用）
     */
    @ApiModelProperty(value = "状态（0：禁用，1：启用）")
    private Integer status;

    List<NursingProjectPlanDto> projectPlans;
}
