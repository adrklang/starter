package com.lhstack.spring.initialize;

import com.lhstack.api.initialize.PluginContextApplicationInitialize;
import com.lhstack.spring.context.SpringPluginContextApplication;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpringConfigurationBeanInitialize implements PluginContextApplicationInitialize<SpringPluginContextApplication> {

    @Override
    public void preInit(SpringPluginContextApplication pluginContext) {

    }

    private void registryBean(Class clazz, SpringPluginContextApplication pluginContext) {
        Method[] declaredMethods = clazz.getMethods();
        Arrays.asList(declaredMethods).forEach(em ->{
            Bean annotation = AnnotationUtils.findAnnotation(em, Bean.class);
            if(annotation != null && em.getParameterCount() == 0){
                try {
                    boolean b = annotation.value().length == 0;
                    if(b || StringUtils.isBlank(annotation.value()[0])){
                        registry(em.getName(),em.invoke(pluginContext.getApplicationContext().getBean(clazz)),pluginContext);
                    }else{
                        registry(annotation.value()[0],em.invoke(pluginContext.getApplicationContext().getBean(clazz)),pluginContext);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(annotation != null && em.getParameterCount() != 0){
                try {
                    registryMoreParameterBean(em,annotation,pluginContext,pluginContext.getApplicationContext().getBean(clazz));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void registryMoreParameterBean(Method method, Bean annotation, SpringPluginContextApplication pluginContext, Object target) throws InvocationTargetException, IllegalAccessException {
        Parameter[] parameters = method.getParameters();
        Object[] parametersBean = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Class o = pluginContext.getBeans().get(parameters[i].getName());
            if(ObjectUtils.isNotEmpty(o)){
                parametersBean[i] = pluginContext.getApplicationContext().getBean(o);
            }else{
                Parameter parameter = parameters[i];
                Qualifier qualifier = parameter.getAnnotation(Qualifier.class);
                if(qualifier != null){
                    Class o1 =  pluginContext.getBeans().get(qualifier.value());
                    if(ObjectUtils.isEmpty(o1)){
                        throw new IllegalArgumentException("bean 不存在");
                    }
                    parametersBean[i] = pluginContext.getApplicationContext().getBean(o1);
                }else{
                    Collection<Class> values =  pluginContext.getBeans().values();
                    List<Class> beans = values.stream().filter(item -> {
                        return parameter.getType().isAssignableFrom(item) || item.isAssignableFrom(parameter.getType());
                    }).collect(Collectors.toList());
                    if(beans.size() > 1 || beans.size() <= 0){
                        throw new IllegalArgumentException("bean不是唯一的,请使用Qualifier约束");
                    }
                    parametersBean[i] = pluginContext.getApplicationContext().getBean(beans.get(0));
                }
            }
        }
        if(annotation.value().length == 0 || StringUtils.isBlank(annotation.value()[0])){
            registry(getKey(method.getName()),method.invoke(target,parametersBean),pluginContext);
        }else{
            registry(annotation.value()[0],method.invoke(target,parametersBean),pluginContext);
        }
    }

    private void registry(String s, Object result, SpringPluginContextApplication pluginContext) {
        GenericApplicationContext applicationContext = pluginContext.getApplicationContext();
        applicationContext.getBeanFactory().registerSingleton(s,result);
       pluginContext.getBeans().put(s,result.getClass());
    }

    public String getKey(String name){
        return name.substring(0,1).toLowerCase() + name.substring(1,name.length());
    }

    @Override
    public void postInit(SpringPluginContextApplication pluginContext) {
        List<Class> beans = pluginContext.getBeans().entrySet().stream().filter(item -> AnnotationUtils.findAnnotation(item.getValue(), Configuration.class) != null)
                .map(Map.Entry::getValue).collect(Collectors.toList());
        beans.forEach(item ->{
            registryBean(item,pluginContext);
        });
    }

    @Override
    public void start(SpringPluginContextApplication pluginContext) {

    }

    @Override
    public long getOrder() {
        return 0;
    }
}
