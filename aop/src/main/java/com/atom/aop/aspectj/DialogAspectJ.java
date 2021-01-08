package com.atom.aop.aspectj;

import com.atom.aop.enums.DialogRunType;
import com.atom.aop.utils.DialogUtils;
import com.atom.aop.utils.log.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class DialogAspectJ {


    @Pointcut("execution(@com.atom.aop.aspectj.VDialog !synthetic *.new(..))")
    public void constructor() {
    }  //构造方法切入点


    @Pointcut("execution(@com.atom.aop.aspectj.VDialog !synthetic * *(..))")
    public void method() {
    }  //方法切入点

    @Around("(method()||constructor()) && @annotation(dialog)")
    public void syncDialogAroundJoinPoint(ProceedingJoinPoint joinPoint, VDialog dialog) throws Throwable {
        // 获取方法中是否有DialogCallback回调
        DialogUtils.DialogCallback callback = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof DialogUtils.DialogCallback) {
                callback = (DialogUtils.DialogCallback) arg;
                break;
            }
        }
        if (dialog.type() == DialogRunType.runBefore) {
            // 先进行dialog显示 , 判断为sure 才进行执行方法体内容 , 最后进行选择回调
            DialogUtils.DialogCallback finalCallback = callback;
            DialogUtils.dialog().set(dialog).callback(new DialogUtils.DialogCallback() {
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
        } else {
            // 先执行方法体内容
            joinPoint.proceed();
            // 如果没有回调 ,也可以显示 ,只是选择就无意义了
            if (callback == null) {
                callback = new DialogUtils.DialogCallback() {
                    @Override
                    public void onSure() {
                        Logger.e("dialog 选择了 onSure");
                    }

                    @Override
                    public void onCancel() {
                        Logger.e("dialog 选择了 onCancel");
                    }
                };
            }
            DialogUtils.dialog().set(dialog).callback(callback).show();
        }
    }
}
