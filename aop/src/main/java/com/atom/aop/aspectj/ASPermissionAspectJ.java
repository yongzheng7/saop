package com.atom.aop.aspectj;

import android.util.Log;

import com.atom.aop.SAOP;
import com.atom.aop.utils.PermissionUtils;

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
public class ASPermissionAspectJ {

    @Pointcut("execution(@com.atom.aop.aspectj.SPermission * *(..))")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(sPermission)")
    public Object aroundJoinPoint(final ProceedingJoinPoint joinPoint, SPermission sPermission) throws Throwable {
        final boolean[] result = {false} ;
        PermissionUtils permission = PermissionUtils.permission(sPermission.value());
        permission.callback(new PermissionUtils.FullCallback() {
            @Override
            public void onGranted(List<String> permissionsGranted) {
                result[0] = true  ;
            }

            @Override
            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                SAOP.getOnPermissionDeniedListener().onDenied(permissionsDenied);
            }
        }).request();
        if(permission.checkPermissions() || result[0]){
            Log.e("MainActivity", "回调  2 ");
            return joinPoint.proceed();
        }else{
            return null ;
        }
    }
}


