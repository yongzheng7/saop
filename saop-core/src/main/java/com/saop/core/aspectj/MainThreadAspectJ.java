package com.saop.core.aspectj;

import android.os.Looper;

import com.saop.annotation.AopMainThread;
import com.saop.core.SAOP;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class MainThreadAspectJ {

    @Pointcut("execution(@com.saop.annotation.AopMainThread !synthetic void *(..))")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(uiThread)")//在连接点进行方法替换
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, AopMainThread uiThread) throws Throwable {
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
