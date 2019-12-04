package com.lhstack.config.proxy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.sql.DataSource;

/**
 * 自动代理DataSource
 */
public class AutoMonitoringConfiguration implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof DataSource){
            return new ProxyDataSource((DataSource) bean);
        }
        return bean;
    }
}
