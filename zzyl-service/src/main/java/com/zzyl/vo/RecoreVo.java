package com.zzyl.vo;

import lombok.Data;

/**
 * @author 阿庆
 */
@Data
public class RecoreVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 类型：（1:退住  2:请假  3:入住）
     */
    private Integer type;

    /**
     * 入住：（流程状态  0:申请入住  1:入住评估  2:入住审批  3:入住配置  4:签约办理）
     * 退住：（流程状态  0:申请退住  1:申请审批  2:解除合同  3:调整账单  4:账单审批  5:退住审批  6:费用算清）
     */
    private Integer flowStatus;

    /**
     * 审核状态
     * 1:通过
     * 2:拒绝
     * 3:驳回
     * 4:撤回
     * 5:撤销
     */
    private Integer status;
    /**
     * 审核意见
     */
    private String option;
    /**
     * 审核步骤
     */
    private String step;
    /**
     * 下一步的操作
     */
    private String nextStep;
    /**
     * 下一个审核人
     */
    private Long nextAssignee;

    /**
     * 当前登录用户id
     */
    private Long userId;

    /**
     * 当前登录用户真实姓名
     */
    private String realName;

    /**
     * 处理类型（0:已审批，1：已处理）
     */
    private Integer handleType;


}
