package com.zzyl.vo.retreat;

import com.zzyl.vo.BalanceVo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author 阿庆
 */
@Data
@ApiModel(description = "退住账单")
public class RetreatBillVo {

    /**
     * 应退（月度账单+小程序的订单）
     */
    private List<DueBack> dueBackList;

    /**
     * 月度欠费
     */
    private List<Arrearage> arrearageList;

    /**
     * 余额（押金+预缴款）
     */
    private BalanceVo balanceVo;

    /**
     * 订单未缴
     */
    private List<Unpaid> unpaidList;

}
