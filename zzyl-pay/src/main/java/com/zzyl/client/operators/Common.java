package com.zzyl.client.operators;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zzyl.client.Config;
import com.zzyl.client.WechatPayHttpClient;
import com.zzyl.client.response.CloseResponse;
import com.zzyl.client.response.QueryResponse;
import com.zzyl.client.response.RefundResponse;
import com.zzyl.utils.EmptyUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @ClassName Common.java
 *  基础交易API封装
 */
@Slf4j
public class Common {

    private Config config;

    public Common(Config config) {
        this.config=config;
    }

    /***
     *  查询交易结果
     * @param outTradeNo 发起支付传递的交易单号
     * @return
     */
    public QueryResponse query(String outTradeNo) throws Exception {
        //请求地址
        String uri ="/v3/pay/transactions/out-trade-no";
        WechatPayHttpClient httpClient = WechatPayHttpClient.builder()
            .mchId(config.getMchId())
            .mchSerialNo(config.getMchSerialNo())
            .apiV3Key(config.getApiV3Key())
            .privateKey(config.getPrivateKey())
            .domain(config.getDomain()+uri)
            .build();
        String uriParams ="/"+outTradeNo+"?mchid="+config.getMchId();
        String body =  httpClient.doGet(uriParams);
        QueryResponse queryResponse = JSONObject.parseObject(body, QueryResponse.class);
        if (!EmptyUtil.isNullOrEmpty(queryResponse.getTradeState())){
            queryResponse.setCode("200");
            queryResponse.setMessage("网关请求成功");
        }
        return queryResponse;
    }

    /***
     *  统一收单交易退款接口查询
     * @param outRefundNo 商户系统内部的退款单号
     * @return
     */
    public RefundResponse queryRefund(String outRefundNo) throws Exception {
        //请求地址
        String uri ="/v3/refund/domestic/refunds";
        WechatPayHttpClient httpClient = WechatPayHttpClient.builder()
            .mchId(config.getMchId())
            .mchSerialNo(config.getMchSerialNo())
            .apiV3Key(config.getApiV3Key())
            .privateKey(config.getPrivateKey())
            .domain(config.getDomain()+uri)
            .build();
        String uriParams ="/"+outRefundNo;
        String body =  httpClient.doGet(uriParams);
        RefundResponse refundResponse = JSONObject.parseObject(body, RefundResponse.class);
        if (!EmptyUtil.isNullOrEmpty(refundResponse.getStatus())){
            refundResponse.setCode("200");
            refundResponse.setMessage("网关请求成功");
        }
        return refundResponse;
    }

    /***
     *  统一收单交易退款接口
     * @param outTradeNo 发起支付传递的交易单号
     * @param amount 退款金额
     * @param outRefundNo 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔
     * @param total 原订单金额
     * @return
     */
    public RefundResponse refund(String outTradeNo, String amount,
                                 String outRefundNo, String total) throws Exception {
        //请求地址
        String uri ="/v3/refund/domestic/refunds";
        WechatPayHttpClient httpClient = WechatPayHttpClient.builder()
            .mchId(config.getMchId())
            .mchSerialNo(config.getMchSerialNo())
            .apiV3Key(config.getApiV3Key())
            .privateKey(config.getPrivateKey())
            .domain(config.getDomain()+uri)
            .build();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode bodyParams = objectMapper.createObjectNode();
        BigDecimal bigDecimal = new BigDecimal(amount);
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(100));
        BigDecimal bigDecimalTotal = new BigDecimal(total);
        BigDecimal multiplyTotal = bigDecimalTotal.multiply(new BigDecimal(100));
        bodyParams.put("out_refund_no", outRefundNo)
            .put("out_trade_no", outTradeNo);
        bodyParams.putObject("amount")
            .put("refund", multiply.intValue())
            .put("total", multiplyTotal.intValue())
            .put("currency", "CNY");
        String body =  httpClient.doPost(bodyParams);
        RefundResponse refundResponse = JSONObject.parseObject(body, RefundResponse.class);
        if (!EmptyUtil.isNullOrEmpty(refundResponse.getStatus())){
            refundResponse.setCode("200");
            refundResponse.setMessage("网关请求成功");
        }
        return refundResponse;
    }

    /***
     *  统一关闭订单
     * 1、商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；
     * 2、系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。
     * @param outTradeNo 外部交易单号
     * @return
     */
    public CloseResponse close(String outTradeNo) throws Exception {
        //请求地址
        String uri ="/v3/pay/transactions/out-trade-no/"+outTradeNo+"/close";
        WechatPayHttpClient httpClient = WechatPayHttpClient.builder()
            .mchId(config.getMchId())
            .mchSerialNo(config.getMchSerialNo())
            .apiV3Key(config.getApiV3Key())
            .privateKey(config.getPrivateKey())
            .domain(config.getDomain()+uri)
            .build();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode bodyParams = objectMapper.createObjectNode();
        bodyParams.put("mchid", config.getMchId());
        try {
            httpClient.doPost(bodyParams);
        } catch (Exception e){}

        CloseResponse closeResponse = new CloseResponse();
        closeResponse.setCode("200");
        closeResponse.setMessage("网关请求成功");
        return closeResponse;
    }
}
