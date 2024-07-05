package com.zzyl.constant;

/**
 * @ClassName TradingCacheConstant.java
 *  交易缓存维护
 */
public class TradingCacheConstant {

    //默认redis等待时间
    public static final int REDIS_WAIT_TIME = 10;

    //默认redis自动释放时间
    public static final int REDIS_LEASETIME = 4;

    //安全组前缀
    public static final String PREFIX = "trading:";

    //分布式锁前缀
    public static final String LOCK_PREFIX = PREFIX+"lock:";

    //创建交易加锁
    public static final String CREATE_PAY = LOCK_PREFIX+ "create_pay";

    //创建退款加锁
    public static final String REFUND_PAY = LOCK_PREFIX+ "refund_pay";

    //创建退款加锁
    public static final String PAY_CHANNEL_LIST = PREFIX+"pay_channel_list&ttl=-1";

    //page分页
    public static final String PAGE= PREFIX+"page";
}
