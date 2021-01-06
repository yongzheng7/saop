package com.atom.aop;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.atom.aop.utils.PermissionUtils;
import com.atom.aop.utils.StringUtils;
import com.atom.aop.utils.log.ILogger;
import com.atom.aop.utils.log.Logger;

public final class SAOP {

    private static Context sContext;

    public static void init(Application application) {
        sContext = application.getApplicationContext();
    }

    public static Context getContext() {
        if (sContext == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 SAOP.init() 初始化！");
        }
        return sContext;
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


}
