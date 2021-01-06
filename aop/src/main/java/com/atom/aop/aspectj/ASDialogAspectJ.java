package com.atom.aop.aspectj;

import com.atom.aop.enums.DialogCallback;
import com.atom.aop.enums.DialogRunType;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ASDialogAspectJ {
    //..,android.app.Activity,..
    @Pointcut("execution(@com.atom.aop.aspectj.SDialog !synthetic * *(.. , com.atom.aop.enums.DialogCallback , ..))")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(sDialog)")
    public Object dialogAroundJoinPoint(ProceedingJoinPoint joinPoint, SDialog sDialog) throws Throwable {
        DialogCallback callback = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof DialogCallback) {
                callback = (DialogCallback) arg;
                break;
            }
        }
        if (callback == null) {
            return joinPoint.proceed();
        } else {
            DialogRunType type = sDialog.type();
            if (type == DialogRunType.runBefore) {
                if (callback.dialogShow()) {
                    return joinPoint.proceed();
                }
                return null;
            } else {
                Object proceed = null;
                try {
                    proceed = joinPoint.proceed();
                    return proceed;
                } finally {
                    callback.dialogShow(proceed);
                }
            }
        }
    }
}
