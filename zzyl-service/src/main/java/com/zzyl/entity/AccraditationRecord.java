package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 阿庆
 */
@Data
@ApiModel(description = "退住实体类")
public class AccraditationRecord extends BaseEntity {

    private Long id;

    /**
     * 审批意见
     */
    @ApiModelProperty(value = "审批意见")
    private String opinion;
    /**
     * 审批类型
     * 1:退住
     * 2:请假
     * 3:入住
     */
    @ApiModelProperty(value = "审批类型")
    private Integer type;
    /**
     * 审批人id
     */
    @ApiModelProperty(value = "审批人id")
    private Long approverId;
    /**
     * 审批人
     */
    @ApiModelProperty(value = "审批人")
    private String approverName;

    /**
     * 审批人角色
     */
    @ApiModelProperty(value = "审批人角色")
    private String approverNameRole;

    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    private Long bussniessId;
    /**
     * 当前审核步骤
     */
    @ApiModelProperty(value = "当前审核步骤")
    private String currentStep;

    /**
     * 下一个审核步骤
     */
    @ApiModelProperty(value = "当前审核步骤")
    private String nextStep;

    /**
     * 审核状态
     * 1:通过
     * 2:拒绝
     * 3:驳回
     * 4:撤回
     * 5:撤销
     */
    @ApiModelProperty(value = "审核状态(1:通过,2:拒绝,3:驳回,4:撤回,5:撤销)")
    private Integer auditStatus;

    /**
     * 下一个审批人
     */
    @ApiModelProperty(value = "下一个审批人")
    private String nextApprover;

    /**
     * 下一个审批人id
     */
    @ApiModelProperty(value = "下一个审批人id")
    private Long nextApproverId;

    /**
     * 下一个审批人角色
     */
    @ApiModelProperty(value = "下一个审批人")
    private String nextApproverRole;

    /**
     * 审核步骤（序号，从1开始）
     */
    @ApiModelProperty(value = "审核步骤（序号，从1开始）")
    private Long stepNo;

    /**
     * 处理类型（0:已审批，1：已处理）
     */
    @ApiModelProperty(value = "处理类型（0:已审批，1：已处理）")
    private Integer handleType;

}
