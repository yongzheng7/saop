package com.atom.aop.aspectj;

import android.os.Looper;

import com.atom.aop.SAOP;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class UIThreadAspectJ {

    @Pointcut("execution(@com.atom.aop.aspectj.AopUIThread !synthetic void *(..))")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(uiThread)")//在连接点进行方法替换
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, AopUIThread uiThread) throws Throwable {
        long delayedTime = uiThread.delayed() < 0 ? 0 : uiThread.delayed();
        if (Looper.getMainLooper() == Looper.myLooper()) {
            if (delayedTime == 0) {
                joinPoint.proceed();
            } else {
                SAOP.handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }, delayedTime);
            }
        } else {
            SAOP.handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        joinPoint.proceed();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }, delayedTime);
        }
    }
}
