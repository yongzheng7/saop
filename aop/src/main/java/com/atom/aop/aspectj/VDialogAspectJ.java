package com.atom.aop.aspectj;

import com.atom.aop.enums.DialogCallback;
import com.atom.aop.enums.DialogRunType;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

//@Aspect
public class VDialogAspectJ {

    //..,android.app.Activity,..
    @Pointcut("execution(@com.atom.aop.aspectj.VDialog !synthetic void *(.. , com.atom.aop.enums.DialogCallback , ..))")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(vDialog)")
    public void dialogAroundJoinPoint(ProceedingJoinPoint joinPoint, VDialog vDialog) throws Throwable {
        DialogCallback callback = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof DialogCallback) {
                callback = (DialogCallback) arg;
                break;
            }
        }
        if (callback == null) {
            joinPoint.proceed();
        } else {
            DialogRunType type = vDialog.type();
            if (type == DialogRunType.runBefore) {
                DialogCallback finalCallback = callback;
                finalCallback.dialogShow(isSure -> {
                    if (isSure) {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                    finalCallback.dialogResult(isSure);
                });
            } else {
                callback.dialogShow(callback::dialogResult, joinPoint.proceed());
            }
        }
    }
}
