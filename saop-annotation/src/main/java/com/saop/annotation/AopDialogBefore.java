package com.saop.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 切面Dialog
 * 当执行一个功能前,进行一些判断条件,如果条件不符合就弹出来dialog,进行提示用户是否执意进行下面的操纵
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD , ElementType.CONSTRUCTOR})
public @interface AopDialogBefore {

    String title();

    String message();

}
