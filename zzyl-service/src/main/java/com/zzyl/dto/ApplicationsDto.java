package com.zzyl.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 阿庆
 */
@Data
@ApiModel("我的申请DTO")
public class ApplicationsDto {

    /**
     * 申请单号
     */
    @ApiModelProperty(value = "申请单号")
    private String code;

    @JsonIgnore
    private Long applicatId;

    /**
     * 单据类别
     */
    @ApiModelProperty(value = "单据类别")
    private Integer type;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Integer status;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

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

}
