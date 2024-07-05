package com.zzyl.vo;

import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NursingTaskVo extends BaseVo {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "护理员id")
    private Long nursingId;

    @ApiModelProperty(value = "老人id")
    private Long elderId;

    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    @ApiModelProperty(value = "床位编号")
    private String bedNumber;

    @ApiModelProperty(value = "任务类型（0：月度任务，1订单任务）")
    private Byte taskType;

    @ApiModelProperty(value = "预计服务时间")
    private LocalDateTime estimatedServerTime;

    @ApiModelProperty(value = "执行记录")
    private String mark;

    @ApiModelProperty(value = "取消原因")
    private String cancelReason;

    @ApiModelProperty(value = "状态  1待执行 2已执行 3已关闭 ")
    private Integer status;

    @ApiModelProperty(value = "关联单据编号")
    private String relNo;

    @ApiModelProperty(value = "执行图片")
    private String taskImage;

    @ApiModelProperty(value = "护理项目名称")
    private String projectName;

    @ApiModelProperty(value = "老人名称")
    private String elderName;

    @ApiModelProperty(value = "护理员名称")
    private List<String> nursingName;

    @ApiModelProperty(value = "护理等级名称")
    private String lName;

    @ApiModelProperty(value = "年龄")
    private String age;

    @ApiModelProperty(value = "头像")
    private String image;

    @ApiModelProperty(value = "性别")
    private String sex;
}