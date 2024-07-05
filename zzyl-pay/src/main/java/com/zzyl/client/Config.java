package com.zzyl.client;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName Config.java
 *  微信的配置文件
 */
@Slf4j
@Data
public class Config {

    //appId
    String appid;

    //商户号
    String mchId;

    //私钥字符串
    String privateKey;

    //商户证书序列号
    String mchSerialNo;

    //V3密钥
    String apiV3Key;

    //请求地址
    String domain;

    //回调地址
    String notifyUrl;

    /***
     *  构建客户端
     * @param mchId  商户号
     * @param privateKey 私钥字符串
     * @param mchSerialNo 商户证书序列号
     * @param apiV3Key V3密钥
     * @return
     */
    @Builder
    public Config(String appid,
                  String mchId,
                  String privateKey,
                  String mchSerialNo,
                  String apiV3Key,
                  String domain,String notifyUrl) {
        this.appid = appid;
        this.mchId = mchId;
        this.privateKey = privateKey;
        this.mchSerialNo = mchSerialNo;
        this.apiV3Key = apiV3Key;
        this.domain=domain;
        this.notifyUrl = notifyUrl;
    }

    public Config() {
    }
}
