package com.atom.aop.aspectj;

import com.atom.aop.enums.DialogCallback;
import com.atom.aop.enums.DialogRunType;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class RDialogAspectJ {
    //..,android.app.Activity,..
    @Pointcut("execution(@com.atom.aop.aspectj.RDialog !synthetic * *(.. , com.atom.aop.enums.DialogCallback , ..))")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(rDialog)")
    public Object dialogAroundJoinPoint(ProceedingJoinPoint joinPoint, RDialog rDialog) throws Throwable {
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
            DialogRunType type = rDialog.type();
            if (type == DialogRunType.runBefore) {
                final boolean[] result = {false};
                result[0] = callback.dialogShow(isSure -> result[0] = isSure);
                if (result[0]) {
                    return joinPoint.proceed();
                }
                return null;
            } else {
                Object proceed = null;
                try {
                    proceed = joinPoint.proceed();
                    return proceed;
                } finally {
                    callback.dialogShow(null, proceed);
                }
            }
        }
    }
}
