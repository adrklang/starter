package com.lhstack.api.bind;

import com.lhstack.api.order.Order;

import java.util.Map;

public interface ParameterBind extends Order {
    static final String PLUGIN_CONTEXT_APPLICATION_INITIALIZE = "com.lhstack.api.bind.ParameterBind";
    Object getObject(Map<String,Object> properties,Object bean);
}
