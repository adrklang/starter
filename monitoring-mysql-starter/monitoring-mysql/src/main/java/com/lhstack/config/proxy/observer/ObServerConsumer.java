package com.lhstack.config.proxy.observer;

/**
 * 实际操作obServer执行sql语句的接口，并获取相应信息
 */
public interface ObServerConsumer {
    Object apply(ObServer obServer) throws Exception;
    ObServerConsumer addMetadata(String key, Object v);
    Object getMetadata();
    ObServerConsumer buildMetaData();
}
