package com.zzyl.vo;

import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class FloorVo extends BaseVo {

    @ApiModelProperty(value = "楼层名称")
    private String name;

    @ApiModelProperty(value = "楼层编号")
    private Integer code;

    @ApiModelProperty(value = "房间")
    private List<RoomVo> roomVoList;

    @ApiModelProperty(value = "绑定的智能设备id列表")
    private List<DeviceVo> deviceVos;
}
