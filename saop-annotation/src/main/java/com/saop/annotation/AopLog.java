package com.saop.annotation;


import com.saop.annotation.enums.LogType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于进行测试进行多长的时间
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface AopLog {

    int priority() default 0;

    String tag() default "";

    LogType type() default LogType.all;
}
