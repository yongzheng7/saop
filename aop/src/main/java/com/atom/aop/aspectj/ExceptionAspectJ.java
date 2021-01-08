package com.atom.aop.aspectj;

import android.text.TextUtils;

import com.atom.aop.SAOP;
import com.atom.aop.api.ExceptionHandler;
import com.atom.aop.utils.ClassUtils;
import com.atom.aop.utils.log.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 自动try-catch的注解切片处理
 */
@Aspect
public class ExceptionAspectJ {

    @Pointcut("within(@com.atom.aop.aspectj.AopException *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.atom.aop.aspectj.AopException * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(exception)")//在连接点进行方法替换
    public Object aroundJoinPoint(final ProceedingJoinPoint joinPoint, AopException exception) throws Throwable {
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            ExceptionHandler exceptionHandler = SAOP.getExceptionHandler();
            if (exceptionHandler!= null) {
                String flag = exception.value();
                if (TextUtils.isEmpty(flag)) {
                    flag = ClassUtils.getMethodName(joinPoint);
                }
                result = exceptionHandler.handleThrowable(flag, e);
            } else {
                Logger.e(e); //默认不做任何处理
            }
        }
        return result;
    }
}
