package com.lhstack.api.bean;

public interface PluginFactoryBean<T> {
    T getObject();
    String getKey();
}
