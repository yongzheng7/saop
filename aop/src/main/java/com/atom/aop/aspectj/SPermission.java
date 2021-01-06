package com.atom.aop.aspectj;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *     desc   : 申请系统权限注解
 *     author : xuexiang
 *     time   : 2018/4/22 下午6:34
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface SPermission {
    /**
     * @return 需要申请权限的集合
     * 通过权限组来便捷申请
     * PermissionConsts.CAMERA
     */
    String[] value();
}