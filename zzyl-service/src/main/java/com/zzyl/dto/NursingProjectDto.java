package com.zzyl.dto;

import com.zzyl.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author: 阿庆
 * @date: 2024/7/4 下午2:09
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class NursingProjectDto extends BaseDto {

    /**
     * 名称
     */
    private String name;

    /**
     * 排序号
     */
    private Integer orderNo;

    /**
     * 单位
     */
    private String unit;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 图片
     */
    private String image;

    /**
     * 护理要求
     */
    private String nursingRequirement;

    /**
     * 状态（0：禁用，1：启用）
     */
    private Integer status;
}
