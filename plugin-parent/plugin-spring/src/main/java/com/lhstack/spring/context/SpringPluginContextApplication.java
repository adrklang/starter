package com.lhstack.spring.context;

import com.lhstack.core.context.PluginContextApplication;
import com.lhstack.core.utils.BeanUtils;
import com.lhstack.spring.autoconfig.SpringPluginAutoConfiguration;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

public class SpringPluginContextApplication extends PluginContextApplication implements SmartLifecycle {

    protected boolean isRunning;

    @Value("${plugin.class.path:F:/plugin}")
    private String path;


    @Autowired
    private GenericApplicationContext applicationContext;

    public GenericApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private static Map<String,Class> beans = new HashMap<>();

    public static Map<String, Class> getBeans() {
        return beans;
    }

    public void init() throws Exception {
        super.scanner(path);
        initFactories();
        initPreLifeContext();
        initPostPlaceHolderProperties();
        preInit();
        initEnvironment();
        registryBeans();
        initAutoConfiguration();
        initBeanBefore();
        super.postInit();
        initBeanAfter();
    }

    private void initAutoConfiguration() {
        List<Object> collect = getFactories().stream().map(item -> item.getProperty(SpringPluginAutoConfiguration.AUTO_CONFIG))
                .filter(StringUtils::isNotBlank)
                .map(item -> {
                    return Arrays.asList(item.split(",")).stream().map(em -> {
                        try {
                            return Class.forName(em).newInstance();
                        } catch (Exception e) {
                            return null;
                        }
                    }).filter(ObjectUtils::isNotEmpty);
                }).flatMap(stream -> stream).collect(Collectors.toList());
        collect.forEach(item ->{
            beans.put(BeanUtils.getKey("",item.getClass()),item.getClass());
            applicationContext.getBeanFactory().registerSingleton(BeanUtils.getKey("",item.getClass()),item);
        });
    }


    private void initBeanAfter() {
        Map<String, Class> beans = getBeans();
        beans.entrySet().forEach(item ->{
            boolean assignableFrom = BeanPostProcessor.class.isAssignableFrom(item.getValue());
            if(assignableFrom){
                applicationContext.getBeanFactory().addBeanPostProcessor((BeanPostProcessor) applicationContext.getBean(item.getKey()));
                applicationContext.getBeanFactory().applyBeanPostProcessorsAfterInitialization((BeanPostProcessor) applicationContext.getBean(item.getKey()),item.getKey());
            }
        });
    }

    private void initBeanBefore() {
        Map<String, Class> registryBeans = getBeans();
        registryBeans.entrySet().forEach(item ->{
            boolean assignableFrom = BeanPostProcessor.class.isAssignableFrom(item.getValue());
            if(assignableFrom){
                applicationContext.getBeanFactory().addBeanPostProcessor((BeanPostProcessor) applicationContext.getBean(item.getKey()));
                applicationContext.getBeanFactory().applyBeanPostProcessorsBeforeInitialization((BeanPostProcessor) applicationContext.getBean(item.getKey()),item.getKey());
            }
        });
    }


    private void registryBeans() throws ClassNotFoundException {
        Set<String> classEss = getClassEss();
        List<? extends Class<?>> clazzss = classEss.stream().map(item -> {
            try {
                return Class.forName(item);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }).filter(ObjectUtils::isNotEmpty).filter(item ->{
            return AnnotationUtils.findAnnotation(item,Component.class) != null;
        }).collect(Collectors.toList());
        clazzss.forEach(item ->{
            String key = AnnotationUtils.findAnnotation(item,Component.class).value();
            key = BeanUtils.getKey(key,item);
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(item);
            AbstractBeanDefinition singleton = beanDefinitionBuilder.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE)
                    .setScope("singleton")
                    .getRawBeanDefinition();
            singleton.setAutowireCandidate(true);
            applicationContext.registerBeanDefinition(key,singleton);
            beans.put(key,item);
        });
    }

    private void initEnvironment() {
        Map<String, Object> aggregationProperties = getAggregationProperties();
        MapPropertySource mapPropertySource = new MapPropertySource("pluginProperties",aggregationProperties);
        applicationContext.getEnvironment().getPropertySources().addLast(mapPropertySource);
    }


    @SneakyThrows
    @Override
    public void start() {
        this.init();
        this.isRunning = true;
    }

    @Override
    public void stop() {
        this.isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

}
