package com.zzyl.vo;

import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 护理项目视图对象
 */
@Data
public class NursingProjectVo extends BaseVo {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "排序号")
    private Integer orderNo;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "护理要求")
    private String nursingRequirement;

    @ApiModelProperty(value = "状态（0：禁用，1：启用）")
    private Integer status;

    @ApiModelProperty(value = "护理项目绑到计划的个数")
    private Integer count;
}
