package com.zzyl.vo.retreat;

import com.zzyl.entity.AccraditationRecord;
import com.zzyl.entity.RescissionContract;
import com.zzyl.entity.Retreat;
import com.zzyl.entity.RetreatBill;
import com.zzyl.vo.CheckInVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 阿庆
 */
@Data
public class TasVo {

    /**
     * 退住信息
     */
    private Retreat retreat;

    /**
     * 入住信息
     */
    private CheckInVo checkIn;

    /**
     * 类型 1退住 2请假 3入住
     */
    private Integer type;

    private String nextApprover;

    /**
     * 审批记录
     */
    private List<AccraditationRecord> accraditationRecords;

    /**
     * 是否展示退住数据
     * 0:其他用户，审核中
     * 1:当前为审核人，可以正常查看数据
     */
    @ApiModelProperty(value = "是否展示退住数据")
    private Integer isShow;

    /**
     * 解除协议
     */
    private RescissionContract rescissionContract;

    /**
     * 退住账单
     */
    private RetreatBillVo retreatBillVo;

    /**
     * 账单json数据
     */
    @ApiModelProperty(value = "账单json数据")
    private String billJson;

    /**
     * 是否显示撤回
     */
    @ApiModelProperty(value = "是否显示撤回")
    private Boolean isRevocation;

    /**
     * 最终的账单数据
     */
    @ApiModelProperty(value = "最终的账单数据")
    private RetreatBill retreatBill;





}
