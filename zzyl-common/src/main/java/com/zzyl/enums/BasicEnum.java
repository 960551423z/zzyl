package com.zzyl.enums;

import com.zzyl.base.IBasicEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *  基础枚举
 */
@Getter
@AllArgsConstructor
public enum BasicEnum implements IBasicEnum {

    SUCCEED(200,"操作成功"),
    SECURITY_ACCESSDENIED_FAIL(401,"权限不足!"),
    SYSYTEM_FAIL(1503,"系统运行异常"),
    VALID_EXCEPTION(1504,"参数校验异常");

    /**
     * 编码
     */
    public int code;
    /**
     * 信息
     */
    public String msg;
}
