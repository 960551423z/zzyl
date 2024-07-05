package com.zzyl.utils;

import cn.hutool.core.date.LocalDateTimeUtil;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author 阿庆
 */
public class CodeUtil {

    /**
     * code生成
     * 样例：TZ 20230630 1010 0001
     *
     * @param prefix        code前缀  比如：退住(TZ)、合同(HT)
     * @param redisTemplate code后四位根据redis自增生成
     * @param expire        生成自增后的失效时间，
     * @return  code码
     */
    public static String generateCode(String prefix, RedisTemplate<String,String> redisTemplate, long expire) {
        String dateStr = LocalDateTimeUtil.format(LocalDateTime.now(), "yyyyMMddHHmmss");
        String code = prefix + dateStr;
        Long increment = redisTemplate.boundValueOps(code).increment();
        //设置过期时间
        if(expire == 0) {
            expire = 5;
        }
        redisTemplate.expire(code, expire, TimeUnit.SECONDS);
        //最终生成的code吗
        return code += increment;
    }

    /**
     * 生成具有给定前缀和时间戳的编码。
     *
     * @param prefix        编码前缀，例如 "TZ" 表示退住或 "HT" 表示合同。
     * @param redisTemplate 用于生成代码末尾四位数字的 Redis 模板。
     * @param expireSeconds 生成的编码的过期时间（秒）。
     * @return 生成的编码。
     */
    public static String generateCode2(String prefix, RedisTemplate<String, String> redisTemplate, long expireSeconds) {
        String dateStr = LocalDateTimeUtil.format(LocalDateTime.now(), "yyyyMMddHHmmss");
        String code = prefix + dateStr;

        // 从 Redis 中递增并获取编码的值，使其唯一。
        Long increment = redisTemplate.boundValueOps(code).increment();

        // 设置编码在 Redis 中的过期时间。
        if (expireSeconds <= 0) {
            expireSeconds = 5; // 如果未提供，默认过期时间为 5 秒。
        }
        redisTemplate.expire(code, expireSeconds, TimeUnit.SECONDS);

        // 最终带有递增值的编码。
        return code + increment;
    }
}