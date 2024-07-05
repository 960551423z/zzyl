package com.zzyl.vo;

import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(description = "房间信息")
public class RoomVo extends BaseVo {

    @ApiModelProperty(value = "房间id")
    private Long id;

    @ApiModelProperty(value = "房间编号")
    private String code;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "房间类型名称")
    private String typeName;

    @ApiModelProperty(value = "楼层id")
    private Long floorId;

    @ApiModelProperty(value = "楼层名称")
    private String floorName;

    @ApiModelProperty(value = "床位")
    private List<BedVo> bedVoList;

    @ApiModelProperty(value = "入住率")
    private Double occupancyRate;

    @ApiModelProperty(value = "总床位数")
    private Integer totalBeds;

    @ApiModelProperty(value = "入住床位数")
    private Integer occupiedBeds;

    @ApiModelProperty(value = "绑定的智能设备id列表")
    private List<DeviceVo> deviceVos;

    private Integer status = 0;


    @ApiModelProperty(value = "床位费用")
    private BigDecimal price;
}
