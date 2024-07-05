package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NursingTask extends BaseEntity {

    /**
     *  护理员ID
     */
    private Long nursingId;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 老人ID
     */
    private Long elderId;

    /**
     * 床位编号
     */
    private String bedNumber;

    /**
     * 任务类型(2:月度任务  1:订单任务)
     */
    private Byte taskType;

    /**
     * 预计服务时间
     */
    private LocalDateTime estimatedServerTime;

    /**
     * 执行记录
     */
    private String mark;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 状态  1待执行 2已执行 3已关闭
     */
    private Integer status;

    /**
     * 关联单据编号
     */
    private String relNo;

    /**
     * 执行图片
     */
    private String taskImage;

    /**
     * 护理项目名称
     */
    private String projectName;

    /**
     * 老人姓名
     */
    private String elderName;

    /**
     * 年龄
     */
    private String age;

    /**
     * 头像
     */
    private String image;

    private String sex;

    private List<String> nursingName;

    private String lName;
}