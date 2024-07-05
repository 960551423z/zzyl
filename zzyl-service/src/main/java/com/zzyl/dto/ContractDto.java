
package com.zzyl.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "ContractDto", description = "合同信息")
public class ContractDto extends BaseDto {
    /**
     * 合同名称
     */
    @ApiModelProperty(value = "合同名称")
    private String name;

    /**
     * 丙方手机号
     */
    @ApiModelProperty(value = "丙方手机号")
    private String memberPhone;

    /**
     * 丙方名称
     */
    @ApiModelProperty(value = "丙方名称")
    private String memberName;

    /**
     * 老人名称
     */
    @ApiModelProperty(value = "老人名称")
    private String elderName;


    /**
     * 合同编号
     */
    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    /**
     * 合同pdf文件地址
     */
    @ApiModelProperty(value = "合同pdf文件地址")
    private String pdfUrl;

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private Long memberId;

    /**
     * 老人id
     */
    @ApiModelProperty(value = "老人id")
    private Long elderId;

    /**
     * 合同开始时间
     */
    @ApiModelProperty(value = "合同开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 合同结束时间
     */
    @ApiModelProperty(value = "合同结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 合同状态
     */
    @ApiModelProperty(value = "合同状态")
    private Integer status;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 级别描述
     */
    @ApiModelProperty(value = "级别描述")
    private String levelDesc;


    /**
     * 入住编号
     */
    @ApiModelProperty(value = "入住编号")
    private String checkInNo;

    @ApiModelProperty(value = "入住申请id")
    private Long checkInId;

    /**
     * 签约时间
     */
    @ApiModelProperty(value = "签约时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signDate;

    /**
     * 解除提交人
     */
    @ApiModelProperty(value = "解除提交人")
    private String releaseSubmitter;

    /**
     * 解除时间
     */
    @ApiModelProperty(value = "解除时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime releaseDate;

    /**
     * 解除pdf文件地址
     */
    @ApiModelProperty(value = "解除pdf文件地址")
    private String releasePdfUrl;


    /**
     * 任务ID
     */
    @ApiModelProperty(value = "任务ID")
    private String taskId;
}


