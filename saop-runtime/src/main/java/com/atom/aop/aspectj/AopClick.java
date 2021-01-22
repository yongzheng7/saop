package com.atom.aop.aspectj;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * View点击拦截
 * 1 拦截多次点击切面 , 根据指定的时间进行判断是否存在密集点击
 * 2 双击,以至于多次点击拦截,按照一次点击处理
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AopClick {

    long value() default 1000;

    int number() default 1 ;

    long interval() default 500 ;
}
