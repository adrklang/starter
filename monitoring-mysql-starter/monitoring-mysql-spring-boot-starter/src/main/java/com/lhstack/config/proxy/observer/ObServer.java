package com.lhstack.config.proxy.observer;

/**
 * 执行sql语句的接口
 */
public interface ObServer {
    Object apply() throws Exception;
}
