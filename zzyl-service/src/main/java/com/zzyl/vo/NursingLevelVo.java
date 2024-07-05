package com.zzyl.vo;

import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "NursingLevelVo对象", description = "护理等级VO类")
public class NursingLevelVo extends BaseVo {

    @ApiModelProperty(value = "等级名称")
    private String name;

    @ApiModelProperty(value = "护理计划名称")
    private String planName;

    @ApiModelProperty(value = "护理计划ID")
    private Long planId;

    @ApiModelProperty(value = "护理费用")
    private BigDecimal fee;

    @ApiModelProperty(value = "状态（0：禁用，1：启用）")
    private Integer status;

    @ApiModelProperty(value = "等级说明")
    private String description;

    @ApiModelProperty(value = "配置ID")
    private Long cid;
}
