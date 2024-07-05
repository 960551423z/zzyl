package com.zzyl.client.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName QueryResponse.java
 *  统一收单线下交易查询返回
 */
@Data
@NoArgsConstructor
public class QueryResponse extends BasicResponse{

    //商户订单号
    @JSONField(name="out_trade_no")
    private String outTradeNo;

    //交易状态:SUCCESS：支付成功，REFUND：转入退款，NOTPAY：未支付，CLOSED：已关闭，REVOKED：已撤销（仅付款码支付会返回）
    //USERPAYING：用户支付中（仅付款码支付会返回），PAYERROR：支付失败（仅付款码支付会返回）
    @JSONField(name="trade_state")
    private String tradeState;

    //交易状态描述
    @JSONField(name="trade_state_desc")
    private String tradeStateDesc;
}
