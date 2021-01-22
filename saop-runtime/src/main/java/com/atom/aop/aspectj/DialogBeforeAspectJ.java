package com.atom.aop.aspectj;

import com.atom.aop.utils.DialogUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class DialogBeforeAspectJ {

    @Pointcut("execution(@com.atom.aop.aspectj.AopDialogBefore !synthetic *.new(..))")
    public void constructor() {
    }  //构造方法切入点


    @Pointcut("execution(@com.atom.aop.aspectj.AopDialogBefore !synthetic void *(..))")
    public void method() {
    }  //方法切入点

    @Around("( method() || constructor() ) && @annotation(dialog)")
    public void syncDialogAroundJoinPoint(ProceedingJoinPoint joinPoint, AopDialogBefore dialog) {
        // 获取方法中是否有DialogCallback回调
        DialogUtils.DialogCallback callback = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof DialogUtils.DialogCallback) {
                callback = (DialogUtils.DialogCallback) arg;
                break;
            }
        }
        // 先进行dialog显示 , 判断为sure 才进行执行方法体内容 , 最后进行选择回调
        DialogUtils.DialogCallback finalCallback = callback;
        DialogUtils
                .dialog()
                .setTitle(dialog.title())
                .setMessage(dialog.message())
                .callback(new DialogUtils.DialogCallback() {
                    @Override
                    public boolean isShow(Object result) {
                        if (finalCallback != null) {
                            return finalCallback.isShow(result);
                        }
                        return true;
                    }

                    @Override
                    public void onSure() {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        } finally {
                            if (finalCallback != null) {
                                finalCallback.onSure();
                            }
                        }
                    }

                    @Override
                    public void onCancel() {
                        if (finalCallback != null) {
                            finalCallback.onCancel();
                        }
                    }
                }).show();
    }
}
