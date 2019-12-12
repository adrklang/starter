package com.lhstack.core.bean;

import com.lhstack.api.context.PluginContext;
import com.lhstack.core.utils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class MoreParameterConstructorPluginBeanContextInitialize extends AbstractPluginBeanContextInitialize {
    @Override
    public void preInit0(PluginContext pluginContext) {
        Set<String> classEss = pluginContext.getClassEss();
        Iterator<String> iterator = classEss.iterator();
        while(iterator.hasNext()){
            String next = iterator.next();
            try{
                registryBean(pluginContext,next);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void registryBean(PluginContext pluginContext, String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        Component annotation = AnnotationUtils.findAnnotation(clazz, Component.class);
        if(annotation != null){
            try{
                clazz.getConstructor();
            }catch (Exception e){
                Constructor<?>[] constructors = clazz.getConstructors();
                if(constructors.length > 0){
                    registryConstructorBean(pluginContext,constructors[0],annotation);
                }
            }
        }
    }

    private void registryConstructorBean(PluginContext pluginContext, Constructor<?> constructor, Component annotation) throws Exception {
        Parameter[] parameters = constructor.getParameters();
        Map<String, Object> beanCache = pluginContext.getBeanCache();
        Object[] parametersBean = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Object o = beanCache.get(parameters[i].getName());
            if(ObjectUtils.isNotEmpty(o)){
                parametersBean[i] = o;
            }else{
                Parameter parameter = parameters[i];
                Qualifier qualifier = parameter.getAnnotation(Qualifier.class);
                if(qualifier != null){
                    Object o1 = beanCache.get(qualifier.value());
                    if(ObjectUtils.isEmpty(o1)){
                        throw new IllegalArgumentException("bean 不存在");
                    }
                    parametersBean[i] = o1;
                }else{
                    Collection<Object> values = beanCache.values();
                    List<Object> beans = values.stream().filter(item -> {
                        return parameter.getType().isAssignableFrom(item.getClass()) || item.getClass().isAssignableFrom(parameter.getType());
                    }).collect(Collectors.toList());
                    if(beans.size() > 1 || beans.size() <= 0){
                        throw new IllegalArgumentException("bean不是唯一的,请使用Qualifier约束,或者bean不存在");
                    }

                }
            }
        }
        Object o = constructor.newInstance(parametersBean);
        super.put(BeanUtils.getKey(annotation.value(),o.getClass()),o,pluginContext);
    }



    @Override
    public long getOrder() {
        return 999;
    }
}
