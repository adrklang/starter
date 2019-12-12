package com.lhstack.api.bean;

public interface PluginBeanPostProcess {
    Object preHandler(String beanName,Object bean);

    Object postHandler(String beanName,Object bean);
}
