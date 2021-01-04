package com.atom.aop.enums;

/**
 * <pre>
 *     desc   :
 *     author : xuexiang
 *     time   : 2018/4/23 上午1:02
 * </pre>
 */
public enum ThreadType {

    /**
     * 单线程池
     */
    Single,

    /**
     * 多线程池
     */
    Fixed,

    /**
     * 磁盘读写线程池(本质上是单线程池）
     */
    Disk,

    /**
     * 网络请求线程池(本质上是多线程池）
     */
    Network
}
