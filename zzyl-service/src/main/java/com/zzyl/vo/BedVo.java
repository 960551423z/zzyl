package com.zzyl.vo;

import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("床位实体类")
public class BedVo extends BaseVo {

    @ApiModelProperty("床位编号")
    private String bedNumber;

    @ApiModelProperty(value = "床位状态: 未入住0, 已入住1 入住申请中2",example = "0")
    private Integer bedStatus;

    @ApiModelProperty("房间ID")
    private Long roomId;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

    @ApiModelProperty(value = "护理员名称")
    private List<String> nursingNames;

    @ApiModelProperty(value = "护理员名称")
    private List<Long> nursingIds;

    @ApiModelProperty(value = "护理员")
    private List<UserVo> userVos;

    @ApiModelProperty(value = "老人姓名")
    private String name;

    @ApiModelProperty(value = "入住配置")
    private CheckInConfigVo checkInConfigVo;


    @ApiModelProperty(value = "楼层名称")
    private String floorName;

    @ApiModelProperty(value = "楼层Id")
    private Long floorId;

    @ApiModelProperty(value = "房间类型名称")
    private String typeName;

    @ApiModelProperty(value = "房间编号")
    private String code;

    @ApiModelProperty(value = "床位费用")
    private BigDecimal price;

    @ApiModelProperty(value = "老人ID")
    private Long elderId;

    @ApiModelProperty(value = "绑定的智能设备id列表")
    private List<DeviceVo> deviceVos;

    private Integer status = 0;

    @ApiModelProperty(value = "等级名称")
    private String lname;

}
