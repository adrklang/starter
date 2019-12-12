package com.lhstack.core.bean;

import com.lhstack.api.bean.PluginFactoryBean;
import com.lhstack.api.context.PluginContext;
import com.lhstack.core.utils.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

public class NormalPluginBeanContextInitialize extends AbstractPluginBeanContextInitialize {

    @Override
    public void preInit0(PluginContext pluginContext) {
        try {
            initBean(pluginContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBean(PluginContext pluginContext) throws Exception {
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

    private void registryBean(PluginContext pluginContext, String beanClass) throws Exception {
        Class<?> clazz = Class.forName(beanClass);
        Component annotation = AnnotationUtils.findAnnotation(clazz, Component.class);
        if(annotation != null){
            try{
                Constructor<?> constructor = clazz.getConstructor();
                if(constructor != null && isNormalBean(clazz)){
                    super.put(BeanUtils.getKey(annotation.value(),clazz),constructor.newInstance(),pluginContext);
                }
            }catch (Exception e){

            }
        }
    }

    private boolean isNormalBean(Class<?> clazz) {
        Class<?>[] interfaces = clazz.getInterfaces();
        if(interfaces.length == 0){
            return true;
        }
        return Arrays.asList(interfaces).stream().anyMatch(item ->item == PluginFactoryBean.class || !PluginFactoryBean.class.isAssignableFrom(item));
    }

    @Override
    public long getOrder() {
        return 997;
    }
}
