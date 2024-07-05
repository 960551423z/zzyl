package com.zzyl.dto;

import com.zzyl.entity.AccraditationRecord;
import com.zzyl.entity.RescissionContract;
import com.zzyl.vo.retreat.RetreatBillVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 阿庆
 */
@Data
public class RetreatDto {

    /**
     * 审核记录
     */
    private AccraditationRecord accraditationRecord;
    /**
     * 解除协议
     */
    private RescissionContract rescissionContract;

    /**
     * 退住编码
     */
    @ApiModelProperty(value = "退住编码")
    private String code;

    /**
     * 操作人id
     */
    /**
     * 退住编码
     */
    @ApiModelProperty(value = "操作人id")
    private String assigneeId;

    /**
     * 账单相关
     */
    private RetreatBillVo retreatBillVo;

    /**
     * 退住账单数据
     */
    @ApiModelProperty(value = "退住账单数据")
    private String billJson;

    /**
     * 任务ID
     */
    @ApiModelProperty(value = "任务ID")
    private String taskId;
}
