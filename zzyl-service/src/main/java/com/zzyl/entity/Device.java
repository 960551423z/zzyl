package com.zzyl.entity;

import com.aliyun.tea.NameInMap;
import com.zzyl.base.BaseEntity;
import com.zzyl.vo.DeviceDataVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class Device extends BaseEntity {

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 绑定位置
     */
    private String bindingLocation;

    /**
     * 位置类型 0 老人 1位置
     */
    private Integer locationType;

    /**
     * 物理位置类型 0楼层 1房间 2床位
     */
    private Integer physicalLocationType;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 位置备注
     */
    private String deviceDescription;

    /**
     * 是否删除
     */
    private Boolean isDeleted;

    /**
     * 备注名称
     */
    private String noteName;

    /**
     * 产品key
     */
    private String productId;

//    private String productName;

    @ApiModelProperty(value = "设备备注名称")
    @NameInMap("Nickname")
    public String nickname;
    @NameInMap("NodeType")
    public Integer nodeType;
    @NameInMap("Owner")
    public Boolean owner;
    @ApiModelProperty(value = "产品key")
    @NameInMap("ProductKey")
    public String productKey;
    @ApiModelProperty(value = "产品名称")
    @NameInMap("ProductName")
    public String productName;
    @NameInMap("Region")
    public String region;
    @NameInMap("Status")
    public String status;
    @NameInMap("UtcActive")
    public String utcActive;
    @NameInMap("UtcCreate")
    public String utcCreate;
    @NameInMap("UtcOnline")
    public String utcOnline;

    private List<DeviceDataVo> deviceDataVos;

}