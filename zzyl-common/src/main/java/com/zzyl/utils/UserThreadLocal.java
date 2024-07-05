package com.zzyl.utils;

import com.alibaba.fastjson.JSONObject;
import com.zzyl.base.BaseVo;
import lombok.extern.slf4j.Slf4j;

/**
 * subjectContent.java
 *  用户主体对象
 */
@Slf4j
public class UserThreadLocal {


    /***
     *  创建线程局部userVO变量
     */
    public static ThreadLocal<String> subjectThreadLocal = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return null;
        }
    };


    // 提供线程局部变量set方法
    public static void setSubject(String subject) {

        subjectThreadLocal.set(subject);
    }
    // 提供线程局部变量get方法
    public static String getSubject() {

        return subjectThreadLocal.get();
    }

    //清空当前线程，防止内存溢出
    public static void removeSubject() {

        subjectThreadLocal.remove();
    }
    private static final ThreadLocal<Long> LOCAL = new ThreadLocal<>();

    private UserThreadLocal() {

    }

    /**
     * 将authUserInfo放到ThreadLocal中
     *
     * @param authUserInfo {@link Long}
     */
    public static void set(Long authUserInfo) {
        LOCAL.set(authUserInfo);
    }

    /**
     * 从ThreadLocal中获取authUserInfo
     */
    public static Long get() {
        return LOCAL.get();
    }

    /**
     * 从当前线程中删除authUserInfo
     */
    public static void remove() {
        LOCAL.remove();
    }

    /**
     * 从当前线程中获取前端用户id
     * @return 用户id
     */
    public static Long getUserId() {
        return LOCAL.get();
    }

    /**
     * 从当前线程中获取前端后端id
     * @return 用户id
     */
    public static Long getMgtUserId() {
        String subject = subjectThreadLocal.get();
        if (ObjectUtil.isEmpty(subject)) {
            return null;
        }
        BaseVo baseVo = JSONObject.parseObject(subject, BaseVo.class);
        return baseVo.getId() ;
    }


}
