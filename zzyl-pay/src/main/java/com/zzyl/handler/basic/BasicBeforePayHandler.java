package com.zzyl.handler.basic;

import com.zzyl.constant.TradingConstant;
import com.zzyl.entity.RefundRecord;
import com.zzyl.entity.Trading;
import com.zzyl.enums.TradingEnum;
import com.zzyl.exception.ProjectException;
import com.zzyl.handler.BeforePayHandler;
import com.zzyl.service.RefundRecordService;
import com.zzyl.service.TradingService;
import com.zzyl.utils.EmptyUtil;
import com.zzyl.utils.UUID;
import com.zzyl.vo.RefundRecordVo;
import com.zzyl.vo.TradingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName BeforePayHandlerImpl.java
 *  阿里交易前置处理接口实现
 */
@Component("beforePayHandler")
public class BasicBeforePayHandler implements BeforePayHandler {

    @Autowired
    TradingService tradingService;

    @Autowired
    RefundRecordService refundRecordService;


    @Override
    public void idempotentCreateTrading(TradingVo tradingVo) throws ProjectException {
        //查询当前订单对应的最近交易单
        Trading trading = tradingService.findTradByProductOrderNo(tradingVo.getProductOrderNo(), tradingVo.getTradingType());
        //交易单为空：之前未对此订单做过支付
        if (EmptyUtil.isNullOrEmpty(trading)) {
            tradingVo.setTradingOrderNo(UUID.getSecureRandom().nextLong());
            return;
        }
        //交易单不为空：判定交易单状态
        if (trading.getTradingState().equals(TradingConstant.TRADE_SUCCESS_4)){
            throw new ProjectException(TradingEnum.TRADING_STATE_SUCCEED);
        }else if (trading.getTradingState().equals(TradingConstant.TRADE_WAIT_BUYER_PAY_1)){
            throw new ProjectException(TradingEnum.TRADING_STATE_PAYING);
        }else if (trading.getTradingState().equals(TradingConstant.TRADE_CLOSED_5)){
            tradingVo.setTradingOrderNo(UUID.getSecureRandom().nextLong());
        }else{
            throw new ProjectException(TradingEnum.PAYING_TRADING_FAIL);
        }
    }

    @Override
    public void idempotentRefundTrading(TradingVo tradingVo) {
        //查询交易单是否为以结算订单
        Trading trading = tradingService.findTradByTradingOrderNo(tradingVo.getTradingOrderNo());
        //交易单不存在，或者不为已结算状态：抛出退款失败异常
        if (EmptyUtil.isNullOrEmpty(trading)|| !TradingConstant.TRADE_SUCCESS_4.equals(trading.getTradingState())){
            throw new ProjectException(TradingEnum.REFUND_FAIL);
        }else {
            tradingVo.setTradingOrderNo(trading.getTradingOrderNo());
            tradingVo.setId(trading.getId());
            tradingVo.setTradingAmount(tradingVo.getOperTionRefund());
        }
        //查询是否有退款中的退款记录
        RefundRecord refundRecord = refundRecordService
                .findRefundRecordByProductOrderNoAndSending(tradingVo.getProductOrderNo());
        if (!EmptyUtil.isNullOrEmpty(refundRecord)){
            throw new ProjectException(TradingEnum.REFUND_FAIL);
        }
    }

    @Override
    public Boolean checkeCreateTrading(TradingVo tradingVo) {
        Boolean flag =null;
        //订单为空
        if (EmptyUtil.isNullOrEmpty(tradingVo)) {
            flag = false;
        }else if (EmptyUtil.isNullOrEmpty(tradingVo.getOpenId())){
            flag = false;
        //订单号为空
        }else if (EmptyUtil.isNullOrEmpty(tradingVo.getProductOrderNo())){
            flag = false;
        }else if (EmptyUtil.isNullOrEmpty(tradingVo.getTradingAmount())){
            flag = false;
        }else {
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean checkeQueryTrading(TradingVo tradingVo) {
        Boolean flag =null;
        //订单为空
        if (EmptyUtil.isNullOrEmpty(tradingVo)) {
            flag = false;
        //企业号为空
        }else if (EmptyUtil.isNullOrEmpty(tradingVo.getEnterpriseId())){
            flag = false;
         //交易号为空
        }else if (EmptyUtil.isNullOrEmpty(tradingVo.getTradingOrderNo())){
            flag = false;
        }else {
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean checkeRefundTrading(TradingVo tradingVo) {

        Boolean flag =null;
        //订单为空
        if (EmptyUtil.isNullOrEmpty(tradingVo)) {
            flag = false;
//        //企业号为空
//        }else if (EmptyUtil.isNullOrEmpty(tradingVo.getEnterpriseId())){
//            flag = false;
        //交易号为空
        }else if (EmptyUtil.isNullOrEmpty(tradingVo.getTradingOrderNo())){
            flag = false;
         //退款请求号为空
        }else if (EmptyUtil.isNullOrEmpty(tradingVo.getOutRequestNo())){
            flag = false;
        //当前退款金额为空
        }else if (EmptyUtil.isNullOrEmpty(tradingVo.getOperTionRefund())){
            flag = false;
        //退款总金额不可超剩余实付总金额
        }else if (tradingVo.getOperTionRefund().compareTo(tradingVo.getTradingAmount()) > 0){
            flag = false;
        }else {
            flag = true;
        }

        return flag;
    }

    @Override
    public Boolean checkeQueryRefundTrading(RefundRecordVo refundRecordVo) {
        Boolean flag =null;
        //订单为空
        if (EmptyUtil.isNullOrEmpty(refundRecordVo)) {
            flag = false;
        //企业号为空
        }else if (EmptyUtil.isNullOrEmpty(refundRecordVo.getEnterpriseId())){
            flag = false;
        //交易号为空
        }else if (EmptyUtil.isNullOrEmpty(refundRecordVo.getTradingOrderNo())){
            flag = false;
         //退款请求号为空
        }else if (EmptyUtil.isNullOrEmpty(refundRecordVo.getRefundNo())){
            flag = false;
        }else {
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean checkeCloseTrading(TradingVo tradingVo) {
        Boolean flag =null;
        //订单为空
        if (EmptyUtil.isNullOrEmpty(tradingVo)) {
            flag = false;
        //企业号为空
        }else if (EmptyUtil.isNullOrEmpty(tradingVo.getEnterpriseId())){
            flag = false;
        //交易号为空
        }else if (EmptyUtil.isNullOrEmpty(tradingVo.getTradingOrderNo())){
            flag = false;
        }else {
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean checkeDownLoadBill(TradingVo tradingVo) {
        Boolean flag =null;
        //订单为空
        if (EmptyUtil.isNullOrEmpty(tradingVo)) {
            flag = false;
        //企业号为空
        }else if (EmptyUtil.isNullOrEmpty(tradingVo.getEnterpriseId())){
            flag = false;
        //账单日期
        }else if (EmptyUtil.isNullOrEmpty(tradingVo.getBillDate())){
            flag = false;
        //账单账单类型
        }else if (EmptyUtil.isNullOrEmpty(tradingVo.getBillType())){
            flag = false;
        }else {
            flag = true;
        }
        return flag;
    }
}
