package com.atom.aop.api;

/**
 * 异常的处理
 */
public interface ExceptionHandler {

    Object handleThrowable(String flag, Throwable throwable);
}
