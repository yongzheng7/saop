package com.atom.aop.aspectj;

import com.atom.aop.enums.LogType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface _Log {

    int priority() default 0;

    String tag() default "";

    LogType type() default LogType.all;
}
