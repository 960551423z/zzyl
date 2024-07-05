package com.zzyl.dto;

import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 阿庆
 */
@Data
@ApiModel(description = "入住实体类")
public class CheckInDto extends BaseDto {

    @ApiModelProperty(value = "保存1 提交2")
    private Integer save;

    @ApiModelProperty(value = "老人")
    private ElderDto elderDto;

    /**
     * 其他申请信息
     */
    @ApiModelProperty(value = "其他信息")
    private String otherApplyInfo;

    /**
     * 健康评估信息
     */
    @ApiModelProperty(value = "健康评估信息")
    private String reviewInfo;

    /**
     * 能力评估
     */
    @ApiModelProperty(value = "能力评估")
    private String reviewInfo1;
    /**
     * 评估报告
     */
    @ApiModelProperty(value = "评估信息")
    private String reviewInfo2;

    /**
     * 入住时间
     */
    @ApiModelProperty(value = "入住时间")
    private LocalDateTime checkInTime;

    /**
     * 家属信息
     */
    @ApiModelProperty(value = "家属信息")
    private List<MemberElderDto> memberElderDtos;

    /**
     * 一寸照片
     */
    @ApiModelProperty(value = "一寸照片")
    private String url1;

    /**
     * 身份证人像面
     */
    @ApiModelProperty(value = "身份证人像面")
    private String url2;

    /**
     * 身份证国徽面
     */
    @ApiModelProperty(value = "身份证国徽面")
    private String url3;


    /**
     * 任务ID
     */
    @ApiModelProperty(value = "任务ID")
    private String taskId;




}
