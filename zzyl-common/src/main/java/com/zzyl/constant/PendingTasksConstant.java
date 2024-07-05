package com.zzyl.constant;

/**
 * @author 阿庆
 */
public class PendingTasksConstant {

    //状态（1：申请中，2:已完成,3:已关闭）
    public static final Integer TASK_STATUS_APPLICATION = 1;
    public static final Integer TASK_STATUS_FINISHED = 2;
    public static final Integer TASK_STATUS_CLOSED = 3;


    //类型（1：退住，2：请假，3：入住）
    public static final Integer TASK_TYPE_RETREAT = 1;
    public static final Integer TASK_TYPE_LEAVE = 2;
    public static final Integer TASK_TYPE_CHECK_IN = 3;


    /**
     * 是否处理完成
     *  0:未处理
     *  1:已处理
     */
    public static final Integer TASK_IS_HANDLE_UNTREATED = 0;
    public static final Integer TASK_IS_HANDLE_FINISHED = 1;




}
