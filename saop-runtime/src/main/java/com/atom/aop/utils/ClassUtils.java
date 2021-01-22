package com.atom.aop.utils;

import com.atom.aop.utils.log.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * class相关工具类
 */
public final class ClassUtils {

    public static String getClassName(Class<?> cls) {
        if (cls == null) {
            return "<UnKnow Class>";
        }
        if (cls.isAnonymousClass()) {
            return getClassName(cls.getEnclosingClass());
        }
        return cls.getSimpleName();
    }


    public static String toString(Object object) {
        if (Logger.getISerializer() != null) {
            return Logger.getISerializer().toString(object);
        } else {
            return StringUtils.toString(object);
        }
    }


    public static String getMethodDescribeInfo(final ProceedingJoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        Class<?> cls = codeSignature.getDeclaringType(); //方法所在类
        String methodName = codeSignature.getName();    //方法名
        return getClassName(cls) + "->" + methodName;
    }

    public static String getMethodName(final ProceedingJoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        Class<?> cls = codeSignature.getDeclaringType(); //方法所在类
        String methodName = codeSignature.getName();    //方法名
        return getClassName(cls) + "." + methodName;
    }




    public static boolean isMethodHasReturnType(Signature signature) {
        return signature instanceof MethodSignature
                && ((MethodSignature) signature).getReturnType() != void.class;
    }

}
