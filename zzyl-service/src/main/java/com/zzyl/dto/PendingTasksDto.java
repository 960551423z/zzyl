package com.zzyl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author 阿庆
 */
@Data
@ApiModel("待办DTO")
public class PendingTasksDto {

    /**
     * 申请单号
     */
    @ApiModelProperty(value = "申请单号")
    private String code;
    /**
     * 申请人
     */
    @ApiModelProperty(value = "申请人")
    private String applicat;
    /**
     * 申请人id
     */
    @ApiModelProperty(value = "申请人id")
    private Long applicatId;
    /**
     * 单据类别（1：退住，2：请假，3：入住）
     */
    @ApiModelProperty(value = "单据类别")
    private Integer type;
    /**
     * 状态（1：申请中，2:已完成，3:已关闭）
     */
    @ApiModelProperty(value = "流程状态")
    private Integer status;

    /**
     * 申请状态
     */
    @ApiModelProperty(value = "申请状态")
    private Integer applyStatus;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页")
    private Integer pageNum;

    /**
     * 每页条数
     */
    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Long assigneeId;

    /**
     * 请求类型(0:待办，1:我的申请)
     */
    @ApiModelProperty(value = "请求类型")
    private Integer reqType;

    /**
     * 是否处理完成
     * 0:未处理
     * 1:已处理
     */
    @ApiModelProperty(value = "是否处理完成(0:未处理,1:已处理)")
    private Integer isHandle;
}
