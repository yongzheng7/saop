package com.saop.api;

import org.aspectj.lang.JoinPoint;


public interface InterceptHandler {

    boolean intercept(int type, JoinPoint joinPoint) throws Throwable;

}
