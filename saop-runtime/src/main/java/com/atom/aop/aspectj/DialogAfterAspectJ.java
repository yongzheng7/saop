package com.atom.aop.aspectj;

import com.atom.aop.utils.DialogUtils;
import com.atom.aop.utils.log.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class DialogAfterAspectJ {


    @Pointcut("execution(@com.atom.aop.aspectj.AopDialogAfter !synthetic *.new(..))")
    public void constructor() {
    }  //构造方法切入点


    @Pointcut("execution(@com.atom.aop.aspectj.AopDialogAfter !synthetic * *(..))")
    public void method() {
    }  //方法切入点

    @Around("( method() || constructor() ) && @annotation(dialog)")
    public Object syncDialogAroundJoinPoint(ProceedingJoinPoint joinPoint, AopDialogAfter dialog) throws Throwable {
        // 获取方法中是否有DialogCallback回调
        DialogUtils.DialogCallback callback = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof DialogUtils.DialogCallback) {
                callback = (DialogUtils.DialogCallback) arg;
                break;
            }
        }
        Object result = null;
        try {
            // 先执行方法体内容
            result = joinPoint.proceed();
            return result;
        } finally {
            // 如果没有回调 ,也可以显示 ,只是选择就无意义了
            final DialogUtils.DialogCallback finalCallback = callback;
            final Object finalResult = result;
            final DialogUtils.DialogCallback temp = new DialogUtils.DialogCallback() {
                @Override
                public boolean isShow(Object result) {
                    if (finalCallback != null) {
                        return finalCallback.isShow(finalResult);
                    }
                    return true;
                }

                @Override
                public void onSure() {
                    if (finalCallback != null) {
                        finalCallback.onSure();
                    }
                }

                @Override
                public void onCancel() {
                    if (finalCallback != null) {
                        finalCallback.onCancel();
                    }
                }
            };
            DialogUtils.dialog().setTitle(dialog.title()).setMessage(dialog.message()).callback(temp).show();
        }
    }
}
