package com.zzyl.handler.wechat;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.zzyl.client.Config;
import com.zzyl.client.Factory;
import com.zzyl.client.response.CloseResponse;
import com.zzyl.client.response.QueryResponse;
import com.zzyl.client.response.RefundResponse;
import com.zzyl.config.WechatPayConfig;
import com.zzyl.constant.TradingConstant;
import com.zzyl.entity.RefundRecord;
import com.zzyl.entity.Trading;
import com.zzyl.enums.TradingEnum;
import com.zzyl.exception.ProjectException;
import com.zzyl.handler.BeforePayHandler;
import com.zzyl.handler.CommonPayHandler;
import com.zzyl.service.RefundRecordService;
import com.zzyl.service.TradingService;
import com.zzyl.utils.*;
import com.zzyl.vo.RefundRecordVo;
import com.zzyl.vo.TradingVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @ClassName BasicPayHandlerImpl.java
 *  微信交易基础类
 */
@Service
@Slf4j
public class WechatCommonPayHandler implements CommonPayHandler {

    @Autowired
    TradingService tradingService;

    @Autowired
    RefundRecordService refundRecordService;

    @Autowired
    BeforePayHandler beforePayHandler;

    @Autowired
    WechatPayConfig wechatPayConfig;

    @Override
    public TradingVo queryTrading(TradingVo tradingVo) {
        //1、查询前置处理：检测交易单参数
        Boolean flag = beforePayHandler.checkeQueryTrading(tradingVo);
        if (!flag){
            throw new ProjectException(TradingEnum.CHECK_TRADING_FAIL);
        }
        Trading tradingHandler = tradingService.findTradByTradingOrderNo(tradingVo.getTradingOrderNo());
        //2、获得微信客户端
        Config config = wechatPayConfig.config();
        //3、配置如果为空，抛出异常
        if (EmptyUtil.isNullOrEmpty(config)){
            throw new ProjectException(TradingEnum.CONFIG_ERROR);
        }
        //4、指定配置文件
        Factory factory = new Factory();
        factory.setOptions(config);
        //5、调用接口
        try {
            QueryResponse queryResponse = factory.Common()
                .query(String.valueOf(tradingVo.getTradingOrderNo()));
            //6、判断响应是否成功
            boolean success = ResponseChecker.success(queryResponse);
            //7、响应成功，分析交易状态
            if (success&&!EmptyUtil.isNullOrEmpty(queryResponse.getTradeState())){
                //交易单状态【1-待付款,2-付款中,3-付款失败,4-已结算,5-取消订单】
                switch (queryResponse.getTradeState()){
                    case TradingConstant.WECHAT_TRADE_CLOSED:
                        tradingHandler.setTradingState(TradingConstant.TRADE_CLOSED_5);break;
                    case TradingConstant.WECHAT_TRADE_REVOKED:
                        tradingHandler.setTradingState(TradingConstant.TRADE_CLOSED_5);break;
                    case TradingConstant.WECHAT_TRADE_SUCCESS:
                        tradingHandler.setTradingState(TradingConstant.TRADE_SUCCESS_4);break;
                    case TradingConstant.WECHAT_TRADE_REFUND:
                        tradingHandler.setTradingState(TradingConstant.TRADE_SUCCESS_4);break;
                    default:
                        flag = false;break;
                }
                //8、修改交易单状态
                if (flag){
                    tradingHandler.setResultCode(queryResponse.getTradeState());
                    tradingHandler.setResultMsg(queryResponse.getTradeStateDesc());
                    tradingHandler.setResultJson(JSONObject.toJSONString(queryResponse));
                    tradingService.saveOrUpdate(tradingHandler);
                    return BeanConv.toBean(tradingHandler,TradingVo.class);
                }else {
//                    log.info("查询微信交易单：{},结果：{}", tradingVo.getTradingOrderNo(),queryResponse.getTradeState());
                }
            }else {
                throw new RuntimeException("查询微信统一下单失败！");
            }
        } catch (Exception e) {
            log.warn("查询微信统一下单失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new RuntimeException("查询微信统一下单失败！");
        }
        return tradingVo;
    }

    @Override
    public TradingVo refundTrading(TradingVo tradingVo) {
        //1、生成退款请求编号
        tradingVo.setOutRequestNo(UUID.getSecureRandom().nextLong());
        Trading tradingHandler = tradingService.findTradByTradingOrderNo(tradingVo.getTradingOrderNo());
        tradingVo.setTradingAmount(tradingHandler.getTradingAmount());
        tradingVo.setRefund(tradingHandler.getRefund());
        tradingVo.setOperTionRefund(tradingHandler.getTradingAmount());
        //2、退款前置处理：检测交易单参数
        Boolean flag = beforePayHandler.checkeRefundTrading(tradingVo);
        if (!flag){
            throw new ProjectException(TradingEnum.REFUND_FAIL);
        }
        //3、退款前置处理：退款幂等性校验
        beforePayHandler.idempotentRefundTrading(tradingVo);
        //4、获得微信客户端
        Config config = wechatPayConfig.config();
        //5、配置如果为空，抛出异常
        if (EmptyUtil.isNullOrEmpty(config)){
            throw new ProjectException(TradingEnum.CONFIG_ERROR);
        }
        //6、指定配置文件
        Factory factory = new Factory();
        factory.setOptions(config);
        //7、调用接口
        try {
            RefundResponse refundResponse = factory.Common().refund(
                    String.valueOf(tradingVo.getTradingOrderNo()),
                    String.valueOf(tradingVo.getOperTionRefund()),
                    String.valueOf(tradingVo.getOutRequestNo()),
                    String.valueOf(tradingVo.getTradingAmount()));
            boolean success = ResponseChecker.success(refundResponse);
            if (success&&String.valueOf(tradingVo.getTradingOrderNo()).equals(refundResponse.getOutTradeNo())){
                //8、指定此交易单是否有退款：YES
//                tradingHandler.setIsRefund(SuperConstant.YES);
////                tradingHandler.setRefund(tradingHandler.getRefund().add(tradingVo.getOperTionRefund()));
////                Trading trading = BeanConv.toBean(tradingVo, Trading.class);
//                tradingHandler.setRemark(tradingVo.getTradingChannel());
////                tradingHandler.setCreateTime(LocalDateTime.now());
//                tradingService.saveOrUpdate(tradingHandler);
                //6、保存退款单信息
                RefundRecord refundRecord = RefundRecord.builder()
                    .enterpriseId(tradingHandler.getEnterpriseId())
                    .refundNo(tradingVo.getOutRequestNo())
                    .refundAmount(tradingVo.getOperTionRefund())
                    .refundCode(refundResponse.getCode())
                    .refundMsg(refundResponse.getMessage())
                    .productOrderNo(tradingHandler.getProductOrderNo())
                    .tradingChannel(tradingHandler.getTradingChannel())
                    .tradingOrderNo(tradingHandler.getTradingOrderNo())
                    .memo(tradingVo.getTradingChannel())
                    .build();
                refundRecord.setCreateTime(LocalDateTime.now());
                refundRecord.setRemark(tradingVo.getTradingChannel());
                refundRecord.setCreateType(tradingVo.getCreateType());
                switch (refundResponse.getStatus()){
                    //退款状态：0-未知错误 1-退款中，2-成功, 3-失败
                    case TradingConstant.WECHAT_REFUND_SUCCESS:
                        refundRecord.setRefundStatus(TradingConstant.REFUND_STATUS_SENDING_1);break;
                    case TradingConstant.WECHAT_REFUND_CLOSED:
                        refundRecord.setRefundStatus(TradingConstant.REFUND_STATUS_CLOSED_3);break;
                    case TradingConstant.WECHAT_REFUND_PROCESSING:
                        refundRecord.setRefundStatus(TradingConstant.REFUND_STATUS_SENDING_1);break;
                    default:
                        refundRecord.setRefundStatus(TradingConstant.REFUND_STATUS_FAIL_0);break;
                }
                refundRecordService.saveOrUpdate(refundRecord);
            }else {
                log.error("网关：微信统一下单退款失败：{},结果：{}", tradingVo.getTradingOrderNo(),
                        JSONObject.toJSONString(refundResponse));
                throw new RuntimeException("网关：微信统一下单退款失败!");
            }
        } catch (Exception e) {
            log.error("微信统一下单退款失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(TradingEnum.REFUND_FAIL);
        }
        return BeanConv.toBean(tradingHandler,TradingVo.class);
    }

    @Override
    public RefundRecordVo queryRefundTrading(RefundRecordVo refundRecordVo) {
        //1、退款前置处理：检测退款单参数
        Boolean flag = beforePayHandler.checkeQueryRefundTrading(refundRecordVo);
        if (!flag){
            throw new ProjectException(TradingEnum.TRAD_QUERY_REFUND_FAIL);
        }
        RefundRecordVo refundRecordHandler = refundRecordService.findByRefundNo(refundRecordVo.getRefundNo());
        //2、获得微信客户端
        Config config = wechatPayConfig.config();
        //3、配置如果为空，抛出异常
        if (EmptyUtil.isNullOrEmpty(config)){
            throw new ProjectException(TradingEnum.CONFIG_ERROR);
        }
        //4、指定配置文件
        Factory factory = new Factory();
        factory.setOptions(config);
        try {
            RefundResponse refundResponse = factory.Common()
                .queryRefund(String.valueOf(refundRecordVo.getRefundNo()));
            //5、判断响应是否成功
            boolean success = ResponseChecker.success(refundResponse);
            //6、查询出的退款状态
            if (success&&TradingConstant.WECHAT_REFUND_SUCCESS.equals(refundResponse.getStatus())){
                refundRecordHandler.setRefundStatus(TradingConstant.REFUND_STATUS_SUCCESS_2);
                refundRecordHandler.setRefundCode(refundResponse.getCode());
                refundRecordHandler.setRefundMsg(refundResponse.getMessage());
                refundRecordService.saveOrUpdate(BeanConv.toBean(refundRecordHandler,RefundRecord.class));
            }else {
                log.error("网关：查询微信统一下单退款失败：{},结果：{}", refundRecordVo.getTradingOrderNo(),
                        JSONObject.toJSONString(refundResponse));
                throw new RuntimeException("网关：查询微信统一下单退款失败！");
            }
        } catch (Exception e) {
            log.warn("查询微信统一下单退款失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new RuntimeException("查询微信统一下单退款失败！");
        }
        return BeanUtil.toBean(refundRecordHandler, RefundRecordVo.class);
    }

    @Override
    public TradingVo closeTrading(TradingVo tradingVo) {
        //1、退款前置处理：检测关闭参数tradingVo
        Boolean flag = beforePayHandler.checkeCloseTrading(tradingVo);
        if (!flag){
            throw new ProjectException(TradingEnum.CLOSE_FAIL);
        }
        Trading tradingHandler = tradingService.findTradByTradingOrderNo(tradingVo.getTradingOrderNo());
        //2、获得微信客户端
        Config config = wechatPayConfig.config();
        //3、配置如果为空，抛出异常
        if (EmptyUtil.isNullOrEmpty(config)){
            throw new ProjectException(TradingEnum.CONFIG_ERROR);
        }
        //4、使用配置
        Factory factory = new Factory();
        factory.setOptions(config);
        try {
            //5、调用三方API关闭订单
            CloseResponse closeResponse = factory.Common()
                    .close(String.valueOf(tradingVo.getTradingOrderNo()));
            //6、关闭订单受理情况
            boolean success = ResponseChecker.success(closeResponse);
            if (success){
                tradingHandler.setTradingState(TradingConstant.TRADE_CLOSED_5);
                tradingService.saveOrUpdate(tradingHandler);
                return BeanConv.toBean(tradingHandler,TradingVo.class);
            }else {
                log.error("网关：微信关闭订单失败：{},结果：{}", tradingVo.getTradingOrderNo(),
                        JSONObject.toJSONString(closeResponse));
                throw  new RuntimeException("网关：微信关闭订单失败!");
            }
        } catch (Exception e) {
            log.warn("微信关闭订单失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw  new ProjectException(TradingEnum.CLOSE_FAIL);
        }
    }

    @Override
    public TradingVo downLoadBill(TradingVo tradingVo) {
        throw  new RuntimeException("未支持：微信关下载账单!");
    }
}
