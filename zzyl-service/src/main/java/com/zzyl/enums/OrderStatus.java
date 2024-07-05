package com.zzyl.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单状态枚举
 */
public enum OrderStatus {



    /**
     * 待支付
     */
    PENDING_PAY(0, "PENDING_PAY"),
    /**
     * 待执行
     */
    PENDING_DO(1, "PENDING_DO"),

    /**
     * 已执行
     */
    DONE(2, "DONE"),
    /**
     * 已完成
     */
    FINISHED(3, "FINISHED"),
    /**
     * 已关闭
     */
    CLOSE(4, "CLOSE"),
    /**
     * 已退款
     */
    REFUND(5, "REFUND");


    OrderStatus(Integer code, String value) {

        this.code = code;
        this.value = value;
    }

    /**
     * 类型编码
     */
    private final Integer code;

    /**
     * 类型值
     */
    private final String value;


    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }


    /**
     * 循环变量
     */
    private static final Map<Integer, OrderStatus> LOOKUP = new HashMap<>();

    //静态初始化
    static {

        for (OrderStatus orderEnum : EnumSet.allOf(OrderStatus.class)) {

            LOOKUP.put(orderEnum.code, orderEnum);
        }
    }

    /**
     * 根据code获取枚举项
     *
     * @param code 值
     * @return 值
     */
    public static OrderStatus lookup(Integer code) {
        return LOOKUP.get(code);
    }
}
