package com.atom.aop.aspectj;

import com.atom.aop.enums.DialogRunType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防止View被连续点击
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD , ElementType.CONSTRUCTOR})
public @interface AopDialog {

    DialogRunType type();

    String title();

    String message();
}