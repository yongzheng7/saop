package com.atom.aop.aspectj;

import com.atom.aop.SAOP;
import com.atom.aop.utils.PermissionUtils;
import com.atom.aop.utils.log.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;


@Aspect
public class VPermissionAspectJ {

    @Pointcut("execution(@com.atom.aop.aspectj.VPermission void *(..))")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(vPermission)")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, VPermission vPermission) throws Throwable {
        PermissionUtils
                .permission(vPermission.value())
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


