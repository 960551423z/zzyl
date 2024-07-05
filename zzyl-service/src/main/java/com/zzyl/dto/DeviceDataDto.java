package com.zzyl.dto;

import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备数据DTO
 */
@Data
public class DeviceDataDto extends BaseDto {

    /**
     * 处理结果
     */
    @ApiModelProperty(value = "处理结果")
    private String processingResult;

    /**
     * 处理者
     */
    @ApiModelProperty(value = "处理者")
    private String processor;

    /**
     * 处理时间
     */
    @ApiModelProperty(value = "处理时间")
    private LocalDateTime processingTime;


}