package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import lombok.Data;

@Data
public class AlertRule extends BaseEntity {

    /**
     * 报警生效时段
     */
    private String alertEffectivePeriod;

    /**
     * 告警规则名称
     */
    private String alertRuleName;

    /**
     * 报警沉默周期
     */
    private Integer alertSilentPeriod;

    /**
     * 数据聚合周期
     */
    private Integer dataAggregationPeriod;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 持续周期
     */
    private Integer duration;

    /**
     * 功能标识
     */
    private String functionId;

    /**
     * 功能名称
     */
    private String functionName;

    /**
     * 模块的key
     */
    private String moduleId;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 运算符
     */
    private String operator;

    /**
     * 所属产品的key
     */
    private String productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 接入设备
     */
    private String relatedDevice;

    /**
     * 备注
     */
    private String remark;

    /**
     * 统计字段
     */
    private String statisticField;

    /**
     * 0 禁用 1启用
     */
    private Integer status;

    /**
     * 阈值
     */
    private Float value;


}