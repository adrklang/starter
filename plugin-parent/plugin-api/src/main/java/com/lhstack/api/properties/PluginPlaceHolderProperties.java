package com.lhstack.api.properties;

import com.lhstack.api.order.Order;

import java.util.Map;

public interface PluginPlaceHolderProperties extends Order {

    static final String PLUGIN_PLACE_HOLDER_PROPERTIES = "com.lhstack.api.properties.PluginPlaceHolderProperties";

    Map<String,Object> getProperties();
    default void init(){

    };
}
