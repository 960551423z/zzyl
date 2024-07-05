package com.zzyl.handler;

import com.zzyl.vo.TradingVo;

/**
 * @ClassName WapPayHandler.java
 *  手机网页支付处理
 */
public interface WapPayHandler extends CommonPayHandler {

    /***
     *  生成交易表单，渲染后自动跳转支付宝或者微信引导用户完成支付
     * @param tradingVo 订单单
     * @return TradingVo对象中的placeOrderJson：阿里云form表单字符串、微信prepay_id标识
     */
    TradingVo wapTrading(TradingVo tradingVo);
}
