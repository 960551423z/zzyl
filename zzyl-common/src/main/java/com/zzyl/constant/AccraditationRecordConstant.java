package com.zzyl.constant;

/**
 * @author 阿庆
 */
public class AccraditationRecordConstant {

    /**
     * 审核状态
     * 1:通过
     * 2:拒绝
     * 3:驳回
     * 4:撤回
     * 5:撤销
     */
    public static final Integer AUDIT_STATUS_PASS = 1;
    public static final Integer AUDIT_STATUS_REJECT = 2;
    public static final Integer AUDIT_STATUS_DISAPPROVE = 3;
    public static final Integer AUDIT_STATUS_WITHDRAWS = 4;
    public static final Integer AUDIT_STATUS_CANCEL = 5;

    /**
     * 记录类型
     * 1:退住
     * 2:请假
     * 3:入住
     */
    public static final Integer RECORD_TYPE_RETREAT = 1;
    public static final Integer RECORD_TYPE_LEAVE = 2;
    public static final Integer RECORD_TYPE_CHECK_IN = 3;

    /**
     * 处理类型（0:已审批，1：已处理）
     */
    public static final Integer RECORD_HANDLE_TYPE_AUDIT = 0;
    public static final Integer RECORD_HANDLE_TYPE_PROCESSED = 1;
}
