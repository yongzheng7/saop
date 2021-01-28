package com.saop.api;

/**
 * 日志记录接口
 */
public interface LogHandler {

    void log(int priority, String tag, String message, Throwable t);

}
