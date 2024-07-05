package com.zzyl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationVo extends BaseVo {

    @ApiModelProperty("预约人")
    private String name;

    @ApiModelProperty("预约人手机号")
    private String mobile;

    @ApiModelProperty("预约时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @ApiModelProperty("探访人")
    private String visitor;

    @ApiModelProperty("预约类型，0：参观预约，1：探访预约")
    private Integer type;

    @ApiModelProperty("预约状态，0：待报道，1：已完成，2：取消，3：过期")
    private Integer status;
}
