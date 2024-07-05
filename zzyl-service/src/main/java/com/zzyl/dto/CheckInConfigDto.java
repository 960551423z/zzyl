package com.zzyl.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "入住配置信息")
public class CheckInConfigDto extends BaseDto {

    @ApiModelProperty(value = "老人ID")
    private Long elderId;

    @ApiModelProperty(value = "入住开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkInStartTime;

    @ApiModelProperty(value = "入住结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkInEndTime;

    @ApiModelProperty(value = "护理等级ID")
    private Long nursingLevelId;

    @ApiModelProperty(value = "入住申请id")
    private Long checkInId;

    @ApiModelProperty(value = "床位Id")
    private Long bedId;

    @ApiModelProperty(value = "费用开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime costStartTime;

    @ApiModelProperty(value = "费用结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime costEndTime;

    @ApiModelProperty(value = "押金金额")
    private BigDecimal depositAmount;

    @ApiModelProperty(value = "护理费用")
    private BigDecimal nursingCost;

    @ApiModelProperty(value = "床位费用")
    private BigDecimal bedCost;

    private BigDecimal otherCost = new BigDecimal(0); //其他费用

    @ApiModelProperty(value = "医保支付")
    private BigDecimal medicalInsurancePayment = new BigDecimal(0);

    @ApiModelProperty(value = "政府补贴")
    private BigDecimal governmentSubsidy = new BigDecimal(0);

    /**
     * 任务ID
     */
    @ApiModelProperty(value = "任务ID")
    private String taskId;

    @ApiModelProperty("房间ID")
    private Long roomId; // 房间ID

    @ApiModelProperty(value = "楼层id")
    private Long floorId;

    @ApiModelProperty(value = "楼层名称")
    private String floorName;

    @ApiModelProperty(value = "房间编号")
    private String code;



}
