package com.zzyl.vo;

import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Alert规则Vo
 */
@Data
public class AlertRuleVo extends BaseVo {

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 相关设备
     */
    private String relatedDevice;
    /**
     * 产品ID
     */
    @ApiModelProperty(value = "产品ID")
    private String productKey;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String productName;

    /**
     * 模块ID
     */
    @ApiModelProperty(value = "模块ID")
    private String moduleId;

    /**
     * 模块名称
     */
    @ApiModelProperty(value = "模块名称")
    private String moduleName;

    /**
     * 功能名称
     */
    @ApiModelProperty(value = "功能名称")
    private String functionName;

    /**
     * 功能ID
     */
    @ApiModelProperty(value = "功能ID")
    private String functionId;

    /**
     * 相关设备
     */
    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "设备名称")
    String deviceName;

    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称")
    private String alertRuleName;

    /**
     * 统计字段
     */
    @ApiModelProperty(value = "统计字段")
    private String statisticField;

    /**
     * 操作符
     */
    @ApiModelProperty(value = "操作符")
    private String operator;

    /**
     * 值
     */
    @ApiModelProperty(value = "值")
    private Float value;

    /**
     * 持续时间
     */
    @ApiModelProperty(value = "持续时间")
    private Integer duration;

    /**
     * 数据聚合周期
     */
    @ApiModelProperty(value = "数据聚合周期")
    private Integer dataAggregationPeriod;

    /**
     * 告警生效周期
     */
    @ApiModelProperty(value = "告警生效周期")
    private String alertEffectivePeriod;

    /**
     * 告警静默期
     */
    @ApiModelProperty(value = "告警静默期")
    private Integer alertSilentPeriod;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态 0-无效 1-有效")
    private Integer status;

    private String rules;
}