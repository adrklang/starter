package com.lhstack.core.bean;

import com.lhstack.api.context.PluginContext;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigurationPluginBeanContextInitialize extends AbstractPluginBeanContextInitialize {
    @Override
    public void preInit0(PluginContext pluginContext) {
        Map<String, Object> beanCache = pluginContext.getBeanCache();
        List<Object> beans = beanCache.entrySet().stream().filter(item -> AnnotationUtils.findAnnotation(item.getValue().getClass(), Configuration.class) != null)
                .map(Map.Entry::getValue).collect(Collectors.toList());
        beans.forEach(item ->{
            registryBean(item,pluginContext);
        });
    }

    private void registryBean(Object item,PluginContext pluginContext) {
        Method[] declaredMethods = item.getClass().getDeclaredMethods();
        Arrays.asList(declaredMethods).forEach(em ->{
            Bean annotation = AnnotationUtils.findAnnotation(em, Bean.class);
            em.setAccessible(true);
            if(annotation != null && em.getParameterCount() == 0){
                try {
                    boolean b = annotation.value().length == 0;
                    if(b || StringUtils.isBlank(annotation.value()[0])){

                        super.put(em.getName(),em.invoke(item),pluginContext);
                    }else{
                        super.put(annotation.value()[0],em.invoke(item),pluginContext);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    registryMoreParameterBean(em,annotation,pluginContext,item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void registryMoreParameterBean(Method method, Bean annotation, PluginContext pluginContext, Object target) throws InvocationTargetException, IllegalAccessException {
        Parameter[] parameters = method.getParameters();
        Object[] parametersBean = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Object o = pluginContext.getBeanCache().get(parameters[i].getName());
            if(ObjectUtils.isNotEmpty(o)){
                parametersBean[i] = o;
            }else{
                Parameter parameter = parameters[i];
                Qualifier qualifier = parameter.getAnnotation(Qualifier.class);
                if(qualifier != null){
                    Object o1 =  pluginContext.getBeanCache().get(qualifier.value());
                    if(ObjectUtils.isEmpty(o1)){
                        throw new IllegalArgumentException("bean 不存在");
                    }
                    parametersBean[i] = o1;
                }else{
                    Collection<Object> values =  pluginContext.getBeanCache().values();
                    List<Object> beans = values.stream().filter(item -> {
                        return parameter.getType().isAssignableFrom(item.getClass()) || item.getClass().isAssignableFrom(parameter.getType());
                    }).collect(Collectors.toList());
                    if(beans.size() > 1 || beans.size() <= 0){
                        throw new IllegalArgumentException("bean不是唯一的,请使用Qualifier约束");
                    }
                    parametersBean[i] = beans.get(0);
                }
            }
        }
        if(annotation.value().length == 0 || StringUtils.isBlank(annotation.value()[0])){
            super.put(getKey(method.getName()),method.invoke(target,parametersBean),pluginContext);
        }else{
            super.put(annotation.value()[0],method.invoke(target,parametersBean),pluginContext);
        }
    }

    public String getKey(String name){
        return name.substring(0,1).toLowerCase() + name.substring(1,name.length());
    }

    @Override
    public long getOrder() {
        return 1000;
    }
}
