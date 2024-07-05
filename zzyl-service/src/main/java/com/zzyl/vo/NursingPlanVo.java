package com.zzyl.vo;

import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "护理计划")
public class NursingPlanVo extends BaseVo {
    /**
     * 排序号
     */
    @ApiModelProperty(value = "护理计划排序号")
    private Integer sortNo;

    @ApiModelProperty(value = "护理计划名称")
    private String planName;


    @ApiModelProperty(value = "状态（0：禁用，1：启用）")
    private Integer status;

    @ApiModelProperty(value = "护理计划项目计划列表")
    List<NursingProjectPlanVo> projectPlans;

    @ApiModelProperty(value = "护理等级id")
    private Long lid;

    @ApiModelProperty(value = "护理等级绑定数量")
    private Integer count;
}
