package com.zzyl.constant;

/**
 * @ClassName TardingConstant.java
 *  交易常量类
 */
public class TradingConstant {

    //【阿里云退款返回状态】
    //REFUND_SUCCESS:成功
    public static final String REFUND_SUCCESS= "REFUND_SUCCESS";

    //【阿里云返回付款状态】
    //TRADE_CLOSED:未付款交易超时关闭，或支付完成后全额退款
    public static final String ALI_TRADE_CLOSED ="TRADE_CLOSED";
    //TRADE_SUCCESS:交易支付成功
    public static final String ALI_TRADE_SUCCESS="TRADE_SUCCESS";
    //TRADE_FINISHED:交易结束不可退款
    public static final String ALI_TRADE_FINISHED ="TRADE_FINISHED";


    //【微信退款返回状态】
    //SUCCESS：退款成功
    public static final String WECHAT_REFUND_SUCCESS ="SUCCESS";
    //CLOSED：退款关闭
    public static final String WECHAT_REFUND_CLOSED="CLOSED";
    //PROCESSING：退款处理中
    public static final String WECHAT_REFUND_PROCESSING ="PROCESSING";
    //ABNORMAL：退款异常
    public static final String WECHAT_REFUND_ABNORMAL ="ABNORMAL";

    //【微信返回付款状态】
    //SUCCESS：支付成功
    public static final String WECHAT_TRADE_SUCCESS ="SUCCESS";
    //REFUND：转入退款
    public static final String WECHAT_TRADE_REFUND ="REFUND";
    //NOTPAY：未支付
    public static final String WECHAT_TRADE_NOTPAY ="NOTPAY";
    //CLOSED：已关闭
    public static final String WECHAT_TRADE_CLOSED ="CLOSED";
    //REVOKED：已撤销（仅付款码支付会返回）
    public static final String WECHAT_TRADE_REVOKED ="REVOKED";
    //USERPAYING：用户支付中（仅付款码支付会返回）
    public static final String WECHAT_TRADE_USERPAYING ="USERPAYING";
    //PAYERROR：支付失败（仅付款码支付会返回）
    public static final String WECHAT_TRADE_PAYERROR ="PAYERROR";

    //【平台:交易渠道】
    //阿里支付
    public static final String TRADING_CHANNEL_ALI_PAY = "ALI_PAY";
    //微信支付
    public static final String TRADING_CHANNEL_WECHAT_PAY = "WECHAT_PAY";
    //现金
    public static final String TRADING_CHANNEL_CASH_PAY = "CASH_PAY";
    //免单挂账【信用渠道】
    public static final String TRADING_CHANNEL_CREDIT_PAY = "CREDIT_PAY";


    //【平台:交易状态】
    //WAIT_BUYER_PAY：待支付(交易创建，等待买家付款)
    public static final Integer TRADE_WAIT_BUYER_PAY_1 =1;
    //SUCCESS：支付成功
    public static final Integer TRADE_SUCCESS_4 =4;
    //CLOSED：已关闭（未付款交易超时关闭,或支付失败)
    public static final Integer TRADE_CLOSED_5 =5;

    //【平台：退款状态】
    //失败
    public static final Integer REFUND_STATUS_FAIL_0= 0;
    //成功
    public static final Integer REFUND_STATUS_SUCCESS_2 = 2;
    //请求中
    public static final Integer REFUND_STATUS_SENDING_1= 1;
    //请求关闭
    public static final Integer REFUND_STATUS_CLOSED_3= 3;

    public static final String ALI_SUCCESS_CODE= "10000";

    public static final String ALI_SUCCESS_MSG= "Success";
}
