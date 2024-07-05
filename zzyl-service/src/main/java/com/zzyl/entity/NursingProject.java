package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 护理项目实体类
 */
@Data
public class NursingProject extends BaseEntity {

    /**
     * 护理名称
     */
    private String name;

    /**
     * 排序
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
     * 图片路径
     */
    private String image;

    /**
     * 护理要求
     */
    private String nursingRequirement;

    /**
     * 状态 （0：禁用，1：启用）
     */
    private Integer status;
}