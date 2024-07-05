package com.zzyl.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "来访信息")
public class Visit extends BaseEntity {

    /**
     * 来访人
     */
    @ApiModelProperty("来访人")
    private String name;

    /**
     * 来访人手机号
     */
    @ApiModelProperty("来访人手机号")
    private String mobile;

    /**
     * 来访时间
     */
    @ApiModelProperty("时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    /**
     * 探访人
     */
    @ApiModelProperty("探访人")
    private String visitor;

    /**
     * 来访类型，0：参观来访，1：探访来访
     */
    @ApiModelProperty("来访类型，0：参观来访，1：探访来访")
    private Integer type;

    /**
     * 来访状态，0：待报道，1：已完成，2：取消，3：过期
     */
    @ApiModelProperty("来访状态，0：待报道，1：已完成，2：取消，3：过期")
    private Integer status;
}
