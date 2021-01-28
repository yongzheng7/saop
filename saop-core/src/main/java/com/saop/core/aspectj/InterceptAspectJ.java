package com.saop.core.aspectj;

import com.saop.annotation.AopIntercept;
import com.saop.core.SAOP;
import com.saop.core.utils.log.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;

/**
 * 自定义拦截切片
 */
@Aspect
public class InterceptAspectJ {

    @Pointcut("within(@com.saop.annotation.AopIntercept *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(!synthetic *.new(..)) && withinAnnotatedClass()")
    public void constructorInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.saop.annotation.AopIntercept * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    } //方法切入点

    @Pointcut("execution(@com.saop.annotation.AopIntercept *.new(..)) || constructorInsideAnnotatedType()")
    public void constructor() {
    } //构造器切入点

    @Around("(method() || constructor()) && @annotation(intercept)")
    public Object aroundJoinPoint(ProceedingJoinPoint joinPoint, AopIntercept intercept) throws Throwable {
        if (SAOP.getInterceptHandler() == null) {
            return joinPoint.proceed(); //没有拦截器不执行切片拦截
        }
        // 执行前 是不是排个序
        int[] value = intercept.value();
        if(intercept.sort()){
            Arrays.sort(value);
        }
        for (int type : value) {
            Logger.e(joinPoint.getSignature().getName() + " --- "+type);
        }
        //执行拦截操作
        for (int type : value) {
            if (SAOP.getInterceptHandler().intercept(type, joinPoint)) { //拦截执行
                return null;
            }
        }
        return joinPoint.proceed();
    }
}
