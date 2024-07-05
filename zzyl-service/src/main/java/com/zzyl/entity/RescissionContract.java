package com.zzyl.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
* rescission_contract 实体类
* </p>
*
* @author 阿庆
*/
@Data
@ApiModel(description = "解除协议")
public class RescissionContract extends BaseEntity implements Serializable  {
    private static final long serialVersionUID = 1L;


    /**
    * 协议URL
    */
    @ApiModelProperty(value = "协议URL")
    private String contractUrl;

    /**
     * 解除合同名称
     */
    @ApiModelProperty(value = "解除合同名称")
    private String rescissionContractName;


    /**
     * 解除合同日期
     */
    @ApiModelProperty(value = "解除合同日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime relieveTime;

    /**
     * 提交人
     */
    @ApiModelProperty(value = "提交人")
    private String commitor;

}
