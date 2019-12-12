package com.lhstack.core.bean;

import com.lhstack.api.context.PluginContext;
import com.lhstack.api.initialize.PluginContextApplicationInitialize;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public abstract class AbstractPluginBeanContextInitialize implements PluginContextApplicationInitialize<PluginContext> {

    public abstract void preInit0(PluginContext pluginContext);

    @Override
    public void preInit(PluginContext pluginContext) {
        String enable = (String) pluginContext.getAggregationProperties().get("com.lhstack.api.initialize.PluginContextApplicationInitialize");
        if(StringUtils.isBlank(enable) || Boolean.parseBoolean(enable)){
            preInit0(pluginContext);
        }
    }

    @Override
    public void postInit(PluginContext pluginContext) {

    }

    protected void put(String key,Object bean,PluginContext pluginContext){
        Map<String, Object> beanCache = pluginContext.getBeanCache();
        Map<String, Object> originBeanCache = pluginContext.getOriginBeanCache();
        beanCache.put(key,bean);
        originBeanCache.put(key,bean);
    }

    @Override
    public void start(PluginContext pluginContext) {

    }
}
