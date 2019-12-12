package com.lhstack.spring.autoconfig;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class SpringPluginAutoConfiguration implements BeanPostProcessor {
    public static final String AUTO_CONFIG = "com.lhstack.api.initialize.PluginAutoConfiguration";

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
