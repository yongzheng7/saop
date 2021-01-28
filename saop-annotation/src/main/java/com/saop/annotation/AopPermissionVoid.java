package com.saop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 调用前进行权限检查,与AopPermission不同的是,这个是同步进行且只能识别返回为void的方法
 * 1 异步,执行和检查是同时进行,判断无权限,直接执行结束即不执行操作,授予权限后需要再次点击
 * 2 同步,执行前会想进行权限申请,申请完,如果权限通过再执行,反之有个别权限未授予则不执行操作
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface AopPermissionVoid {

    String[] value();

}