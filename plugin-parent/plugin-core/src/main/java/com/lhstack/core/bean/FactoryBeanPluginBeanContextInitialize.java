package com.lhstack.core.bean;

import com.lhstack.api.bean.PluginFactoryBean;
import com.lhstack.api.context.PluginContext;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FactoryBeanPluginBeanContextInitialize extends AbstractPluginBeanContextInitialize {
    @Override
    public void preInit0(PluginContext pluginContext) {
        Set<String> classEss = pluginContext.getClassEss();
        List<PluginFactoryBean> factoryBeans = classEss.stream().map(item -> {
            try {
                return Class.forName(item);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).filter(ObjectUtils::isNotEmpty).filter(item -> PluginFactoryBean.class.isAssignableFrom(item) && AnnotationUtils.findAnnotation(item, Component.class) != null)
                .map(item -> {
                    try {
                        return (PluginFactoryBean) item.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }).filter(ObjectUtils::isNotEmpty)
                .collect(Collectors.toList());
        factoryBeans.forEach(item ->{
            pluginContext.getBeanCache().put(item.getKey(),item.getObject());
        });
    }

    @Override
    public long getOrder() {
        return 998;
    }
}
