package com.zzyl.client;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import com.zzyl.utils.EmptyUtil;
import com.zzyl.utils.ExceptionsUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

/**
 * @ClassName WechatPayHttpClient.java
 *  支付宝支付远程调用对象
 */
@Slf4j
@Data
@NoArgsConstructor
public class WechatPayHttpClient {

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

    @Builder
    public WechatPayHttpClient(String mchId,
                               String privateKey,
                               String mchSerialNo,
                               String apiV3Key,
                               String domain) {
        this.mchId = mchId;
        this.privateKey = privateKey;
        this.mchSerialNo = mchSerialNo;
        this.apiV3Key = apiV3Key;
        this.domain = domain;
    }


    /***
     *  构建CloseableHttpClient远程请求对象
     * @return: org.apache.http.impl.client.CloseableHttpClient
     */
    private CloseableHttpClient createHttpClient() throws UnsupportedEncodingException {
        // 加载商户私钥（privateKey：私钥字符串）
        PrivateKey merchantPrivateKey = PemUtil
                .loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes("utf-8")));
        // 获取证书管理器实例
        CertificatesManager certificatesManager = CertificatesManager.getInstance();
        // 向证书管理器增加需要自动更新平台证书的商户信息
        try {
            certificatesManager.putMerchant(mchId, new WechatPay2Credentials(mchId,
                    new PrivateKeySigner(mchSerialNo, merchantPrivateKey)), apiV3Key.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("流处理异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            return null;
        } catch (GeneralSecurityException e) {
            log.error("微信支付权限异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            return null;
        } catch (HttpCodeException e) {
            log.error("微信支付请求异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            return null;
        }
        Verifier verifier = null;
        try {
            verifier = certificatesManager.getVerifier(mchId);
        } catch (NotFoundException e) {
            log.error("微信支付验证者创建异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            return null;
        }
        // 初始化httpClient
        return com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder.create()
            .withMerchant(mchId, mchSerialNo, merchantPrivateKey)
            .withValidator(new WechatPay2Validator(verifier))
            .build();
    }

    /***
     *  支持post请求的远程调用
     * @param params 携带请求参数
     * @return  返回字符串
     */
    public String doPost(ObjectNode params) throws IOException {
        HttpPost httpPost = new HttpPost("https://"+domain);
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type","application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(bos, params);

        httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
        CloseableHttpResponse response = this.createHttpClient().execute(httpPost);
        log.info("关闭交易单：网关返回 -->" + JSON.toJSONString(response));
        if (!EmptyUtil.isNullOrEmpty(response)){
            return EntityUtils.toString(response.getEntity());
        }
        return null;
    }

    /***
     *  支持get请求的远程调用
     * @param param 在路径中请求的参数
     * @return
     * @return: 返回字符串
     */
    public String doGet(String param) throws URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder("https://"+domain+param);
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.addHeader("Accept", "application/json");
        CloseableHttpResponse response = this.createHttpClient().execute(httpGet);
        return EntityUtils.toString(response.getEntity());
    }

}
