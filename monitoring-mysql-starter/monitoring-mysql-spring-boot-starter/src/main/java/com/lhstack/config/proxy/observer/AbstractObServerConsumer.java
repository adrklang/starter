package com.lhstack.config.proxy.observer;

import java.util.HashMap;
import java.util.Map;

/**
 * 抽取通用方法
 */
public abstract class AbstractObServerConsumer implements ObServerConsumer {
    protected Map<String,Object> metaData = new HashMap<>();

    @Override
    public ObServerConsumer addMetadata(String key, Object v) {
        metaData.put(key,v);
        return this;
    }

    @Override
    public Object getMetadata() {
        return metaData;
    }

    @Override
    public ObServerConsumer buildMetaData() {
        metaData = new HashMap<>();
        return this;
    }
}
