package com.zzyl.vo;

import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "NursingProjectPlanVo对象", description = "护理项目计划VO")
public class NursingProjectPlanVo extends BaseVo {

    /**
     * 计划id
     */
    private Long planId;

    /**
     * 项目id
     */
    private Long projectId;

    @ApiModelProperty(value = "计划执行时间")
    private String executeTime;

    @ApiModelProperty(value = "执行周期  0 天 1 周 2月")
    private Integer executeCycle;

    @ApiModelProperty(value = "执行频次")
    private Integer executeFrequency;

    @ApiModelProperty(value = "项目名称")
    private String projectName;
}
