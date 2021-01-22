package com.atom.aop.aspectj;

import com.atom.aop.enums.ThreadType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 工作线程操作
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AopWorkThread {
    ThreadType value() default ThreadType.Fixed;
}
