package com.zzyl.handler;

import com.zzyl.exception.ProjectException;
import com.zzyl.vo.RefundRecordVo;
import com.zzyl.vo.TradingVo;

/**
 * @ClassName IdempotentHandler.java
 *  交易前置处理接口
 */

public interface BeforePayHandler {


    /***
     *  CreateTrading交易幂等性
     * @param tradingVo 交易订单
     * @return
     */
    void idempotentCreateTrading(TradingVo tradingVo) throws ProjectException;

    /***
     *  CreateTrading交易单参数校验
     * @param tradingVo 交易订单
     * @return
     */
    Boolean checkeCreateTrading(TradingVo tradingVo);

    /***
     *  QueryTrading交易单参数校验
     * @param tradingVo 交易订单
     * @return
     */
    Boolean checkeQueryTrading(TradingVo tradingVo);

    /***
     *  RefundTrading退款交易幂等性
     * @param tradingVo 交易订单
     * @return
     */
    void idempotentRefundTrading(TradingVo tradingVo);

    /***
     *  RefundTrading退款交易单参数校验
     * @param tradingVo 交易订单
     * @return
     */
    Boolean checkeRefundTrading(TradingVo tradingVo);


    /***
     *  QueryRefundTrading交易单参数校验
     * @param refundRecordVo 退款记录
     * @return
     */
    Boolean checkeQueryRefundTrading(RefundRecordVo refundRecordVo);

    /***
     *  closeTradin交易单参数校验c
     * @param tradingVo 交易订单
     * @return
     */
    Boolean checkeCloseTrading(TradingVo tradingVo);

    /***
     *  DownLoadBill下载订单交易
     * @param tradingVo 交易订单
     * @return
     */
    Boolean checkeDownLoadBill(TradingVo tradingVo);
}
