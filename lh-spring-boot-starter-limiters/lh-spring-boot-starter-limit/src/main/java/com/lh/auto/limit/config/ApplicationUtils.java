package com.lh.auto.limit.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationUtils implements ApplicationContextAware {
    private static ApplicationContext staticApplicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        staticApplicationContext = applicationContext;
    }

    public static Object getBean(String name){
        Object bean = staticApplicationContext.getBean(name);
        return bean;
    }
}
