package com.saop.core.aspectj;

import android.os.Looper;
import android.os.Trace;

import com.saop.annotation.AopLog;
import com.saop.annotation.enums.LogType;
import com.saop.core.utils.ClassUtils;
import com.saop.core.utils.log.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;

import java.util.concurrent.TimeUnit;

/**
 * 埋点记录
 */
@Aspect
public class LogAspectJ {

    @Pointcut("within(@com.saop.annotation.AopLog *)")
    public void type() {
    } //方法切入点


    @Pointcut("execution(@com.saop.annotation.AopLog !synthetic * *(..))")
    public void method() {
    } //方法切入点

    @Pointcut("execution(@com.saop.annotation.AopLog !synthetic *.new(..))")
    public void constructor() {
    } //构造器切入点

    @Around("(type() || constructor() || method()) && @annotation(log)")
    public Object logAndExecute(ProceedingJoinPoint joinPoint, AopLog log) throws Throwable {
        if (log.type() != LogType.after) {
            enterMethod(joinPoint, log);
        }
        long startNanos = System.nanoTime();
        Object result = joinPoint.proceed();
        long stopNanos = System.nanoTime();
        if (log.type() != LogType.before) {
            exitMethod(joinPoint, log, result, TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos));
        }
        return result;
    }


    /**
     * 方法执行前切入
     */
    private void enterMethod(ProceedingJoinPoint joinPoint, AopLog log) {
        if (!Logger.isDebug()) {
            return;
        }
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        Class<?> cls = codeSignature.getDeclaringType(); //方法所在类
        String methodName = codeSignature.getName();    //方法名
        String[] parameterNames = codeSignature.getParameterNames(); //方法参数名集合
        Object[] parameterValues = joinPoint.getArgs(); //方法参数集合

        //记录并打印方法的信息

        StringBuilder builder = new StringBuilder((log.tag().isEmpty() ? cls.getSimpleName() : log.tag()) + " \u21E2 ");
        builder.append(methodName).append('(');
        for (int i = 0; i < parameterValues.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(parameterNames[i]).append('=');
            builder.append(ClassUtils.toString(parameterValues[i]));
        }
        builder.append(')');

        if (Looper.myLooper() != Looper.getMainLooper()) {
            builder.append(" [Thread:\"").append(Thread.currentThread().getName()).append("\"]");
        }

        Logger.log(log.priority(), ClassUtils.getClassName(cls), builder.toString());

        final String section = builder.toString().substring(2);
        Trace.beginSection(section);
    }


    /**
     * 方法执行完毕，切出
     *
     * @param joinPoint
     * @param result       方法执行后的结果
     * @param lengthMillis 执行方法所需要的时间
     */
    private void exitMethod(ProceedingJoinPoint joinPoint, AopLog log, Object result, long lengthMillis) {
        if (!Logger.isDebug()) {
            return;
        }
        Trace.endSection();
        Signature signature = joinPoint.getSignature();
        Class<?> cls = signature.getDeclaringType();
        String methodName = signature.getName();
        boolean hasReturnType = ClassUtils.isMethodHasReturnType(signature);

        StringBuilder builder = new StringBuilder((log.tag().isEmpty() ? cls.getSimpleName() : log.tag()) + " \u21E0 ")
                .append(methodName)
                .append(" [")
                .append(lengthMillis)
                .append("ms]");

        if (hasReturnType) {
            builder.append(" = ");
            builder.append(ClassUtils.toString(result));
        }

        Logger.log(log.priority(), ClassUtils.getClassName(cls), builder.toString());
    }


}
