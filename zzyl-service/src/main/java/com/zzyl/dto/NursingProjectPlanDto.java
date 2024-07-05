package com.zzyl.dto;

import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "NursingProjectPlanDto", description = "护理项目计划数据传输对象")
public class NursingProjectPlanDto extends BaseDto {

    /**
     * 计划id
     */
    @ApiModelProperty(value = "计划id")
    private Long planId;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Long projectId;

    /**
     * 计划执行时间
     */
    @ApiModelProperty(value = "计划执行时间")
    private String executeTime;

    /**
     * 执行周期
     */
    @ApiModelProperty(value = "执行周期  0 天 1 周 2月")
    private Integer executeCycle;

    /**
     * 执行频次
     */
    @ApiModelProperty(value = "执行频次")
    private Integer executeFrequency;
}

