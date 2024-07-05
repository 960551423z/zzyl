package com.zzyl.vo;

import com.aliyun.tea.NameInMap;
import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DeviceVo extends BaseVo {

    @ApiModelProperty(value = "位置类型")
    Integer locationType;

    @ApiModelProperty(value = "绑定位置")
    Long bindingLocation;

    @ApiModelProperty(value = "设备名称")
    String deviceName;

    @ApiModelProperty(value = "物理位置类型")
    Integer physicalLocationType;

    @ApiModelProperty(value = "设备ID")
    String deviceId;

    @ApiModelProperty(value = "位置名称回显字段")
    private String deviceDescription;

    @NameInMap("DeviceSecret")
    public String deviceSecret;

    @NameInMap("FirmwareVersion")
    public String firmwareVersion;

    @NameInMap("GmtActive")
    public String gmtActive;

    @NameInMap("GmtCreate")
    public String gmtCreate;

    @NameInMap("GmtOnline")
    public String gmtOnline;

    @NameInMap("IotId")
    public String iotId;

    @NameInMap("IpAddress")
    public String ipAddress;

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
