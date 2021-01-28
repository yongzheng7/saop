package com.saop.api;


public interface ExceptionHandler {

    Object handleThrowable(String flag, Throwable throwable);
}
