package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "NursingProjectPlan对象", description = "护理项目计划实体类")
public class NursingProjectPlan extends BaseEntity {

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
    @ApiModelProperty(value = "执行周期")
    private Integer executeCycle;

    /**
     * 执行频次
     */
    @ApiModelProperty(value = "执行频次")
    private Integer executeFrequency;
}
