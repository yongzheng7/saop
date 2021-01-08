package com.atom.aop.aspectj;

import com.atom.aop.utils.CheckUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CheckAspectJ {


    @Pointcut("execution(@com.atom.aop.aspectj.AopCheck !synthetic *.new(..))")
    public void constructor() {
    }  //构造方法切入点


    @Pointcut("execution(@com.atom.aop.aspectj.AopCheck !synthetic * *(..))")
    public void method() {
    }  //方法切入点

    @Around("( method() || constructor() ) && @annotation(check)")
    public Object syncDialogAroundJoinPoint(ProceedingJoinPoint joinPoint, AopCheck check) throws Throwable {
        // 获取方法中是否有DialogCallback回调
        CheckUtils.CheckCallback callback = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof CheckUtils.CheckCallback) {
                callback = (CheckUtils.CheckCallback) arg;
                break;
            }
        }
        if (callback == null) {
            return joinPoint.proceed();
        } else {
            if (callback.isCorrect(check)) {
                return joinPoint.proceed();
            } else {
                callback.checkSkip(check);
                return null;
            }
        }
    }
}
