package com.zzyl.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* @author 阿庆
*/
@Data
public class PendingTasks implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    private String id;

    private Long checkInId;

    /**
    * 申请人
    */
    @ApiModelProperty(value = "申请人")
    private String applicat;

    /**
     * 申请人id
     */
    @ApiModelProperty(value = "申请人角色名称")
    private Long applicatId;

    /**
    * 申请时间
    */
    @ApiModelProperty(value = "申请时间")
    private LocalDateTime applicationTime;

    /**
    * 编号
    */
    @ApiModelProperty(value = "编号")
    private String code;

    /**
    * 状态（1：申请中，2:已完成,3:已关闭）
    */
    @ApiModelProperty(value = "状态")
    private Integer status;

    /**
     * 操作人ID
     */
    @ApiModelProperty(value = "操作人ID")
    private Long assigneeId;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String assignee;

    /**
    * 标题
    */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
    * 类型（1：退住，2：请假，3：入住）
    */
    @ApiModelProperty(value = "类型")
    private Integer type;

    /**
     * 完成时间
     */
    @ApiModelProperty(value = "完成时间")
    private LocalDateTime finishTime;

    /**
     * 是否处理完成
     * 0:未处理
     * 1:已处理
     */
    @ApiModelProperty(value = "是否处理完成(0:未处理,1:已处理)")
    private Integer isHandle;

    /**
     * 审核步骤
     */
    @ApiModelProperty(value = "审核步骤")
    private Integer stepNo;


    /**
     * 流程状态
     *  0:申请退住
     *  1:申请审批
     *  2:解除合同
     *  3:调整账单
     *  4:账单审批
     *  5:退住审批
     *  6:费用算清
     */
    @ApiModelProperty(value = "流程状态")
    private Integer flowStatus;


}
