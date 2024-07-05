package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import com.zzyl.vo.NursingProjectPlanVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 实体类：NursingPlan（护理计划）
 */
@ApiModel("护理计划")
@Data
public class NursingPlan extends BaseEntity {

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

    /**
     * 护理项目计划列表
     */
    @ApiModelProperty(value = "护理项目计划")
    List<NursingProjectPlanVo> projectPlans;

    /**
     * 护理等级ID
     */
    @ApiModelProperty(value = "护理等级id")
    private Long lid;
}
