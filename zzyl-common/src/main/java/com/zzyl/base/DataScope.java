package com.zzyl.base;

import java.lang.annotation.*;

/**
 * DataScope
 * @describe: 数据权限过滤注解
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    public String deptAlias() default "";

    public String userAlias() default "";
}
