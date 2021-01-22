/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.atom.saop;

import android.app.Application;

import com.atom.aop.SAOP;
import com.atom.aop.api.ExceptionHandler;
import com.atom.aop.api.InterceptHandler;
import com.atom.aop.utils.PermissionUtils;
import com.atom.aop.utils.StringUtils;
import com.atom.aop.utils.log.Logger;

import org.aspectj.lang.JoinPoint;

import java.util.List;

/**
 * <pre>
 *     desc   :
 *     author : xuexiang
 *     time   : 2018/4/22 下午7:13
 * </pre>
 */
public class App extends Application {

    public static final String TRY_CATCH_KEY = "getNumber";

    public static final int INTERCEPT_LOGIN = 10;

    @Override
    public void onCreate() {
        super.onCreate();
        SAOP.init(this);
        SAOP.debug(true);
        SAOP.setOnPermissionDeniedListener(new PermissionUtils.OnPermissionDeniedListener() {
            @Override
            public void onDenied(List<String> permissionsDenied) {
                String s = StringUtils.listToString(permissionsDenied);
                Logger.e("权限就是没有 自己跳转去申请吧" + s);
            }
        });

        SAOP.setExceptionHandler((flag, throwable) -> throwable.getLocalizedMessage() + " --- > 发生异常啦, 我看到了 , 给你一个正常的你去完把");

        SAOP.setInterceptHandler((type, joinPoint) -> {
            switch (type) {
                case 1: {
                    Logger.e("我这个拦截器 啥也不干 , 我就看看 算是个打个日志啥的" );
                    break;
                }
                case 2: {
                    Logger.e("我这个拦截器 , 不许过 , 我拦截了" );
                    return true ;
                }
            }
            return false;
        });
    }
}
