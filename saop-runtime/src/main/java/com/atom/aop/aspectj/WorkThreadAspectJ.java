package com.atom.aop.aspectj;

import android.os.Looper;

import com.atom.aop.SAOP;
import com.atom.aop.utils.ClassUtils;
import com.atom.aop.utils.log.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.concurrent.Callable;


@Aspect
public class WorkThreadAspectJ {

    @Pointcut("within(@com.atom.aop.aspectj.AopWorkThread *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.atom.aop.aspectj.AopWorkThread * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(workThread)")//在连接点进行方法替换
    public Object aroundJoinPoint(final ProceedingJoinPoint joinPoint, AopWorkThread workThread) throws Throwable {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            return joinPoint.proceed();
        } else {
            Logger.d(ClassUtils.getMethodDescribeInfo(joinPoint) + " \u21E2 [当前线程]:" + Thread.currentThread().getName() + "，正在切换到子线程！");
            Object result = null;
            switch(workThread.value()) {
                case Single:
                    result = SAOP.singleThreadPool().submit(new Callable() {
                        @Override
                        public Object call() {
                            return proceed(joinPoint);
                        }
                    }).get();
                    break;
                case Fixed:
                    result = SAOP.fixedThreadPool().submit(new Callable() {
                        @Override
                        public Object call() {
                            return proceed(joinPoint);
                        }
                    }).get();
                    break;
            }
            return result;
        }
    }

    private Object proceed(final ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null ;
    }
}
