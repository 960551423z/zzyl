package com.zzyl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitVo extends BaseVo {

    @ApiModelProperty("来访ID")
    private Long id;

    @ApiModelProperty("来访人")
    private String name;

    @ApiModelProperty("来访人手机号")
    private String mobile;

    @ApiModelProperty("时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @ApiModelProperty("探访人")
    private String visitor;

    @ApiModelProperty("来访类型，0：参观来访，1：探访来访")
    private Integer type;

    @ApiModelProperty("来访状态，0：待报道，1：已完成，2：取消，3：过期")
    private Integer status;

}
