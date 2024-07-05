package com.zzyl.vo;

import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomTypeVo extends BaseVo {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("房型名称")
    private String name;

    @ApiModelProperty("床位数量")
    private Integer bedCount;

    @ApiModelProperty("房间数量")
    private Integer roomCount;

    @ApiModelProperty("床位费用")
    private BigDecimal price;

    @ApiModelProperty("创建人")
    private String creator;

    @ApiModelProperty("介绍")
    private String introduction;

    @ApiModelProperty("照片")
    private String photo;

    @ApiModelProperty("状态，0：禁用，1：启用")
    private Integer status;
}
