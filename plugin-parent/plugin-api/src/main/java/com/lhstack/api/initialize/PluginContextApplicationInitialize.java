package com.lhstack.api.initialize;

import com.lhstack.api.context.PluginContext;
import com.lhstack.api.order.Order;

public interface PluginContextApplicationInitialize<T extends PluginContext> extends Order {

    static final String PLUGIN_CONTEXT_APPLICATION_INITIALIZE = "com.lhstack.api.initialize.PluginContextApplicationInitialize";
    void preInit(T pluginContext);

    void postInit(T pluginContext);

    void start(T pluginContext);
}
