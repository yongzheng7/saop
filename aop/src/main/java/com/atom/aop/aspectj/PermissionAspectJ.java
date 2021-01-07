package com.atom.aop.aspectj;

import com.atom.aop.SAOP;
import com.atom.aop.utils.PermissionUtils;
import com.atom.aop.utils.log.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

/**
 * <pre>
 *     desc   : 申请系统权限切片，根据注解值申请所需运行权限
 *     author : xuexiang
 *     time   : 2018/4/22 下午8:50
 * </pre>
 */
@Aspect
public class PermissionAspectJ {

    @Pointcut("execution(@com.atom.aop.aspectj._Permission * *(..))")
    public void method_async() {
    }  //方法切入点

    @Around("method_async() && @annotation(permission)")
    public Object aroundJoinPoint(final ProceedingJoinPoint joinPoint, _Permission permission) throws Throwable {
        final boolean[] result = {false};
        PermissionUtils
                .permission(permission.value())
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        result[0] = true;
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        result[0] = false;
                        SAOP.getOnPermissionDeniedListener().onDenied(permissionsDenied);
                    }

                }).request();
        if (result[0]) {
            return joinPoint.proceed();
        } else {
            return null;
        }
    }


    @Pointcut("execution(@com.atom.aop.aspectj.VPermission void *(..))")
    public void method_sync() {
    }

    @Around("method_sync() && @annotation(permission)")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, VPermission permission) throws Throwable {
        PermissionUtils
                .permission(permission.value())
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        try {
                            //获得权限，执行原方法
                            joinPoint.proceed();
                        } catch (Throwable e) {
                            e.printStackTrace();
                            Logger.e(e);
                        }
                    }
                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {

                        SAOP.getOnPermissionDeniedListener().onDenied(permissionsDenied);
                    }
                }).request();
    }
}


