package com.zzyl.constant;

/**
* 用户表缓存常量
*/
public class UserCacheConstant {

    /**
     * 缓存父包
     */
    public static final String PREFIX= "user:";

    /**
     * 缓存父包
     */
    public static final String BASIC= PREFIX+"basic";

    /**
     * 分布式锁前缀
     */
    public static final String LOCK_PREFIX = PREFIX+"lock:";

    /**
     * page分页
     */
    public static final String PAGE= PREFIX+"page";

    /**
     * list下拉框
     */
    public static final String LIST= PREFIX+"list";

    /**
     * 用户登录
     */
    public static final String LOGIN= PREFIX+"login";

    /**
     * 建立用户与会话唯一标识之间的关系,用于判断剔除
     */
    public static final String USER_TOKEN = PREFIX+"user-token:";

    /**
     * 建立会话唯一标识与jwtToken之间的关系,用于令牌续期
     */
    public static final String JWT_TOKEN = PREFIX+"jwt-token:";

}
