package com.zzyl.handler.wechat;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import com.zzyl.client.Config;
import com.zzyl.client.Factory;
import com.zzyl.client.response.WapPayResponse;
import com.zzyl.constant.SuperConstant;
import com.zzyl.constant.TradingConstant;
import com.zzyl.entity.Trading;
import com.zzyl.enums.TradingEnum;
import com.zzyl.exception.ProjectException;
import com.zzyl.handler.WapPayHandler;
import com.zzyl.utils.BeanConv;
import com.zzyl.utils.EmptyUtil;
import com.zzyl.utils.ExceptionsUtil;
import com.zzyl.utils.ResponseChecker;
import com.zzyl.vo.TradingVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

/**
 * @ClassName WechatWapPayHandler.java
 *  Jsapi支付处理
 */
@Slf4j
@Component
public class WechatWapPayHandler extends WechatCommonPayHandler implements WapPayHandler {

    @Override
    public TradingVo wapTrading(TradingVo tradingVo){
        //1、交易前置处理：检测交易单参数
        Boolean flag = beforePayHandler.checkeCreateTrading(tradingVo);
        if (!flag){
            throw new ProjectException(TradingEnum.CHECK_TRADING_FAIL);
        }
        //2、交易前置处理：幂等性处理
        beforePayHandler.idempotentCreateTrading(tradingVo);
        //3、获得微信客户端
        Config config = wechatPayConfig.config();
        //4、配置如果为空，抛出异常
        if (EmptyUtil.isNullOrEmpty(config)){
            throw new RuntimeException("微信支付配置为空！");
        }
        //5、指定配置文件
        Factory factory = new Factory();
        factory.setOptions(config);

        try {
            //6、调用接口
            WapPayResponse wapPayResponse = factory.Wap().pay(
                String.valueOf(tradingVo.getTradingOrderNo()),
                String.valueOf(tradingVo.getTradingAmount()),
                tradingVo.getMemo(),
                tradingVo.getOpenId());
            //7、检查网关响应结果
            boolean success = ResponseChecker.success(wapPayResponse);
            if (success&&!EmptyUtil.isNullOrEmpty(wapPayResponse.getPrepayId())){
                tradingVo.setIsRefund(SuperConstant.NO);
                tradingVo.setEnterpriseId(Long.valueOf(config.getMchId()));
                tradingVo.setPlaceOrderCode(wapPayResponse.getCode());
                tradingVo.setPlaceOrderMsg(wapPayResponse.getMessage());
                //封装JSAPI调起支付的参数（给前端使用）
                Long timeStamp = System.currentTimeMillis() / 1000;
                String nonceStr = IdUtil.simpleUUID();
                String packages = "prepay_id=" + wapPayResponse.getPrepayId();
                JsapiPayParam jsapiPayParam = JsapiPayParam.builder()
                        .timeStamp(timeStamp)
                        .appId(config.getAppid())
                        .nonceStr(nonceStr)
                        .packages(packages)
                        .paySign(this.createPaySign(config, timeStamp, nonceStr, packages))
                        .build();


                //设置jsapi调起支付的参数
                tradingVo.setPlaceOrderJson(JSONUtil.toJsonStr(jsapiPayParam));
//                tradingVo.setPlaceOrderJson(wapPayResponse.getPrepayId());
                tradingVo.setTradingState(TradingConstant.TRADE_WAIT_BUYER_PAY_1);
                Trading trading = BeanConv.toBean(tradingVo, Trading.class);
                trading.setTradingChannel("小程序支付");
                tradingService.saveOrUpdate(trading);
                return BeanConv.toBean(trading, TradingVo.class);
            }else {
                log.error("网关：支付宝手机网页支付统一下单创建：{},结果：{}", tradingVo.getTradingOrderNo(),
                        JSONObject.toJSONString(wapPayResponse));
                throw new RuntimeException("网关：微信手机网页支付统一下单创建失败！");
            }
        } catch (Exception e) {
            log.error("微信手机网页支付统一下单创建失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new RuntimeException("微信手机网页支付统一下单创建失败");
        }
    }

    /**
     * 生成
     *
     * @param client    微信client对象
     * @param timeStamp 时间戳
     * @param nonceStr  随机数
     * @param packages  预支付字符串
     * @return 签名字符串
     * @throws Exception 不处理异常，全部抛出
     */
    private String createPaySign(Config client, Long timeStamp, String nonceStr, String packages) throws Exception {
        Signature sign = Signature.getInstance("SHA256withRSA");
        // 加载商户私钥
        PrivateKey privateKey = PemUtil
                .loadPrivateKey(new ByteArrayInputStream(client.getPrivateKey().getBytes(CharsetUtil.CHARSET_UTF_8)));
        sign.initSign(privateKey);
        String message = StrUtil.format("{}\n{}\n{}\n{}\n",
                client.getAppid(),
                timeStamp,
                nonceStr,
                packages);
        sign.update(message.getBytes());
        return Base64.getEncoder().encodeToString(sign.sign());
    }

}
