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

package com.atom.aop.aspectj;

import android.view.View;

import com.atom.aop.SAOP;
import com.atom.aop.utils.ClassUtils;
import com.atom.aop.utils.log.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 单次和多次并存
 */
@Aspect
public class ClickAspectJ {

    @Pointcut("execution(@com.atom.aop.aspectj.AopClick !synthetic * *(..))")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(click)")
    public Object aroundJoinPoint(ProceedingJoinPoint joinPoint, AopClick click) throws Throwable {
        View view = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof View) {
                view = (View) arg;
                break;
            }
        }
        if (view != null && click.number() > 0) {
            if (isCanClick(view, click)) {
                return joinPoint.proceed();
            } else {
                if (Logger.isDebug()) {
                    int number = click.number();
                    if (number == 1) {
                        Logger.e(ClassUtils.getMethodDescribeInfo(joinPoint) + "模式:预防多次点击   ，View id:" + view.getId());
                    } else {
                        Logger.e(ClassUtils.getMethodDescribeInfo(joinPoint) + "模式:允许多次点击 ," + number + "次生效，View id:" + view.getId());
                    }
                }
            }
        }
        return null;
    }

    /**
     * 最近一次点击的时间
     */
    private static long sLastClickTime = 0;
    /**
     * 最近一次点击的控件ID
     */
    private static int sLastClickViewId = -1;
    /**
     * 某个按钮的点击次数
     */
    private static int sClickNumber = 0;

    /**
     * 是否是快速点击
     */
    public static boolean isCanClick(View v, AopClick click) {
        long currTime = System.currentTimeMillis();
        int currViewId = v.getId();
        if (currViewId != sLastClickViewId) {
            sLastClickViewId = currViewId;
            sClickNumber = 0;
            sLastClickTime = 0;
        }
        if (click.number() == 1) {
            long spaceTime = currTime - sLastClickTime;
            if (spaceTime > click.value()) {
                sLastClickTime = currTime;
                return true;
            }
        } else {
            if (sLastClickTime == 0) {
                sClickNumber++;
                sLastClickTime = currTime;
            } else {
                long interval = currTime - sLastClickTime;
                if (interval < click.interval()) {
                    sClickNumber++;
                    sLastClickTime = currTime;
                } else {
                    sLastClickTime = currTime;
                    sClickNumber = 0;
                    sClickNumber++;
                }
                if (sClickNumber == click.number()) {
                    // 成功后 再次初始化
                    sClickNumber = 0;
                    sLastClickTime = 0;
                    return true;
                }
            }
        }
        return false;
    }
}
