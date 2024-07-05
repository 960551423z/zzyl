package com.zzyl.client.operators;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zzyl.client.Config;
import com.zzyl.client.WechatPayHttpClient;
import com.zzyl.client.response.WapPayResponse;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @ClassName Wap.java
 *  手机网页支付
 */
@Slf4j
public class Wap {

    private Config config;

    public Wap(Config config) {
        this.config=config;
    }

    /***
     *  生成交易表单，渲染后自动跳转支付宝或者微信引导用户完成支付
     * @param outTradeNo  外部订单【业务系统中间的交易单号】
     * @param amount    交易的金额【单位：分】
     * @param description   商品的描述
     * @param openId    用户在当前公众号下的OpenID
     * @return TradingVo对象中的placeOrderJson：阿里云form表单字符串、微信prepay_id标识
     */
    public WapPayResponse pay(String outTradeNo, String amount,
                              String description, String openId) throws Exception {
        //请求地址
        String uri ="/v3/pay/transactions/jsapi";
        //发起对应的请求
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
        bodyParams.put("mchid",config.getMchId())
                .put("appid",config.getAppid() )
                .put("description", description)
                .put("notify_url", config.getNotifyUrl())
                .put("out_trade_no", outTradeNo);
        //交易金额
        bodyParams.putObject("amount")
                .put("total", multiply.intValue());
        //付款者OpenId信息
        bodyParams.putObject("payer").put("openid",openId);
        String body =  httpClient.doPost(bodyParams);
        WapPayResponse wapPayResponse = JSONObject.parseObject(body, WapPayResponse.class);
        wapPayResponse.setCode("200");
        return wapPayResponse;
    }


}
