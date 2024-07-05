package com.zzyl.dto;

import com.zzyl.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 阿庆
 */
@Data
public class RetreatReqDto extends BaseEntity {

    /**
     * 退住编码
     */
    @ApiModelProperty(value = "退住编码")
    private String retreatCode;
    /**
     * 老人姓名
     */
    @ApiModelProperty(value = "老人姓名")
    private String name;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    private String idCardNo;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
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
