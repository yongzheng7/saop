package com.saop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 切面Dialog
 * 当执行完一个功能后, 进行dialog显示,提示用户是否进行跳转或者只进行提示用户执行完成,等操作
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD , ElementType.CONSTRUCTOR})
public @interface AopDialogAfter {

    String title();

    String message();
}
