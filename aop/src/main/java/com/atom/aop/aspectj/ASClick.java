package com.atom.aop.aspectj;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防止View被连续点击
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ASClick {

    /**
     * 此次点击集合动作和下次的间隔时间
     * number == 1 起效
     * 如果number >1
     * 则判定为 连续点击 number次起效
     * 这时 value 失效 interval 起效
     * @return
     */
    long value() default 1000;

    int number() default 1 ;

    long interval() default 500 ;
}
