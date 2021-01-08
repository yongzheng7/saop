package com.atom.aop;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.atom.aop.api.ExceptionHandler;
import com.atom.aop.api.InterceptHandler;
import com.atom.aop.utils.PermissionUtils;
import com.atom.aop.utils.StringUtils;
import com.atom.aop.utils.log.ILogger;
import com.atom.aop.utils.log.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public final class SAOP {

    private static Context sContext;
    private static Handler handler;

    private static ExecutorService mSingleThreadPool;
    private static ExecutorService mFixedThreadPool;

    private static InterceptHandler sInterceptHandler;
    private static ExceptionHandler sExceptionHandler;

    public static void init(Application application) {
        sContext = application.getApplicationContext();
    }

    public static Context getContext() {
        if (sContext == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 SAOP.init() 初始化！");
        }
        return sContext;
    }

    public static Handler handler() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }

    public static ExecutorService singleThreadPool() {
        if (mSingleThreadPool == null) {
            // TODO 添加对外的接口 以便自定义
            mSingleThreadPool = Executors.newSingleThreadExecutor();
        }
        return mSingleThreadPool;
    }

    public static ExecutorService fixedThreadPool() {
        if (mFixedThreadPool == null) {
            // TODO 添加对外的接口 以便自定义
            mFixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }
        return mFixedThreadPool;
    }

    public static void debug(boolean isDebug) {
        Logger.debug(isDebug);
    }

    public static void debug(String tag) {
        Logger.debug(tag);
    }

    public static void setPriority(int priority) {
        Logger.setPriority(priority);
    }

    public static void setISerializer(@NonNull StringUtils.ISerializer sISerializer) {
        Logger.setISerializer(sISerializer);
    }

    public static void setLogger(@NonNull ILogger logger) {
        Logger.setLogger(logger);
    }

    /**
     * 权限申请被拒绝的监听
     */
    private static PermissionUtils.OnPermissionDeniedListener sOnPermissionDeniedListener;

    public static void setOnPermissionDeniedListener(@NonNull PermissionUtils.OnPermissionDeniedListener listener) {
        SAOP.sOnPermissionDeniedListener = listener;
    }

    public static PermissionUtils.OnPermissionDeniedListener getOnPermissionDeniedListener() {
        return sOnPermissionDeniedListener;
    }

    /**
     * 自定义注解拦截器
     *
     * @param interceptHandler
     */
    public static void setInterceptHandler(@NonNull InterceptHandler interceptHandler) {
        SAOP.sInterceptHandler = interceptHandler;
    }

    public static InterceptHandler getInterceptHandler() {
        return sInterceptHandler;
    }

    /**
     * 自定义异常收集器
     *
     * @param exceptionHandler
     */
    public static void setExceptionHandler(@NonNull ExceptionHandler exceptionHandler) {
        SAOP.sExceptionHandler = exceptionHandler;
    }

    public static ExceptionHandler getExceptionHandler() {
        return sExceptionHandler;
    }

}
