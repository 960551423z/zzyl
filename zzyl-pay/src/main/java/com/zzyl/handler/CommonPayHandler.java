package com.zzyl.handler;

import com.zzyl.vo.RefundRecordVo;
import com.zzyl.vo.TradingVo;

/**
 * @ClassName CommonPayHandler.java
 *  基础交易处理接口
 */
public interface CommonPayHandler {

    /***
     *  统一收单线下交易查询
     * 该接口提供所有支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑。
     * @param tradingVo 交易单
     * @return
     */
    TradingVo queryTrading(TradingVo tradingVo);

    /***
     *  统一收单交易退款接口
     * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
     * 将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
     * @param tradingVo 交易单
     * @return
     */
    TradingVo refundTrading(TradingVo tradingVo);

    /***
     *  统一收单交易退款查询接口
     * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
     * 将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
     * @param refundRecordVo 退款交易单
     * @return
     */
    RefundRecordVo queryRefundTrading(RefundRecordVo refundRecordVo) ;

    /***
     *  统一关闭订单
     * 1、商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；
     * 2、系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。
     * @param tradingVo 交易单
     * @return
     */
    TradingVo closeTrading(TradingVo tradingVo);

    /***
     *  为方便商户快速查账，支持商户通过本接口获取商户离线账单下载地址
     * @param tradingVo 退款交易单
     * @return
     */
    TradingVo downLoadBill(TradingVo tradingVo);


}
