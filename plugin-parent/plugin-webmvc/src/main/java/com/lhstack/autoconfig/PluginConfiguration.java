package com.lhstack.autoconfig;

import com.lhstack.utils.ContextClassScannerUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class PluginConfiguration implements SmartLifecycle {

    private boolean isRunning;

    private Map<String,Class> beans = new HashMap<>();

    @Autowired
    private GenericApplicationContext applicationContext;

    private static final String PLUGIN_CLASS_PATH_NAME = "plugin.factories";

    private List<Properties> properties = new ArrayList<>();

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Value("${plugin.class.path:E:\\plugins}")
    private String path;

    @SneakyThrows
    @Override
    public void start() {
        scanner();
        this.isRunning = true;
    }

    /**
     * 加载配置文件
     * @throws IOException
     */
    private void initLoadProperties() throws IOException {
        URLClassLoader urlClassLoader = ContextClassScannerUtils.getURLClassLoader();
        Enumeration<URL> resources = urlClassLoader.getResources("META-INF/" + PLUGIN_CLASS_PATH_NAME);
        while(resources.hasMoreElements()){
            URL url = resources.nextElement();
            InputStream in = (InputStream) url.getContent();
            Properties properties = new Properties();
            properties.load(in);
            this.properties.add(properties);
        }
    }


    public void scanner() throws Exception {
        /**
         * 扫描插件
         */
        Set<String> scanner = ContextClassScannerUtils.scanner(path);
        initLoadProperties();
        Iterator<String> iterator = scanner.iterator();
        while(iterator.hasNext()){
            String clazz = iterator.next();
            initComponent(clazz);
        }
        /**
         * 初始化controller
         */
        initController();
    }

    private void initController() {
        Set<Map.Entry<String, Class>> entries = beans.entrySet();
        for (Map.Entry<String, Class> entry : entries) {
            Class value = entry.getValue();
            Controller annotation = AnnotationUtils.findAnnotation(value, Controller.class);
            if(null != annotation){
                RequestMapping requestMapping = AnnotationUtils.findAnnotation(value, RequestMapping.class);
                if(null != requestMapping){
                    initHandlerMapping(requestMapping,value,entry.getKey());
                }
            }
        }
    }

    private void initHandlerMapping(RequestMapping requestMapping, Class value,String key) {
        Method[] methods = value.getMethods();
        for (Method method : methods) {
            GetMapping getMapping = AnnotationUtils.getAnnotation(method, GetMapping.class);
            if(getMapping != null){
                registryGetHandlerMapping(requestMapping,getMapping,method,key);
                continue;
            }
            PostMapping postMapping = AnnotationUtils.getAnnotation(method, PostMapping.class);
            if(postMapping != null){
                registryPostHandlerMapping(requestMapping,postMapping,method,key);
                continue;
            }
            PutMapping putMapping = AnnotationUtils.getAnnotation(method, PutMapping.class);
            if(putMapping != null){
                registryPutHandlerMapping(requestMapping,putMapping,method,key);
                continue;
            }
            DeleteMapping deleteMapping = AnnotationUtils.getAnnotation(method,DeleteMapping.class);
            if(deleteMapping != null){
                registryDeleteHandlerMapping(requestMapping,deleteMapping,method,key);
                continue;
            }

            RequestMapping requestMappings = AnnotationUtils.getAnnotation(method,RequestMapping.class);
            if(requestMappings != null){
                registryRequestHandlerMapping(requestMapping,requestMappings,method,key);
            }
        }
    }

    private void registryRequestHandlerMapping(RequestMapping requestMapping, RequestMapping requestMappings, Method method, String key) {
        if(requestMapping.value().length == 0){
            RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths(requestMappings.value())
                    .produces(requestMappings.produces())
                    .params(requestMappings.params())
                    .headers(requestMappings.headers())
                    .consumes(requestMappings.consumes())
                    .build();
            registryHandlerMapping(requestMappingInfo,method,key);
        }else{
            for (String s : requestMapping.value()) {
                RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths(builderPath(s,requestMappings.value()))
                        .produces(requestMappings.produces())
                        .params(requestMappings.params())
                        .headers(requestMappings.headers())
                        .consumes(requestMappings.consumes())
                        .build();
                registryHandlerMapping(requestMappingInfo,method,key);
            }
        }
    }

    private void registryDeleteHandlerMapping(RequestMapping requestMapping, DeleteMapping deleteMapping, Method method, String key) {
        if(requestMapping.value().length == 0){
            RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths(deleteMapping.value())
                    .produces(deleteMapping.produces())
                    .params(deleteMapping.params())
                    .methods(RequestMethod.DELETE)
                    .headers(deleteMapping.headers())
                    .consumes(deleteMapping.consumes())
                    .build();
            registryHandlerMapping(requestMappingInfo,method,key);
        }else{
            for (String s : requestMapping.value()) {
                RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths(builderPath(s,deleteMapping.value()))
                        .produces(deleteMapping.produces())
                        .params(deleteMapping.params())
                        .methods(RequestMethod.DELETE)
                        .headers(deleteMapping.headers())
                        .consumes(deleteMapping.consumes())
                        .build();
                registryHandlerMapping(requestMappingInfo,method,key);
            }
        }
    }

    private void registryPutHandlerMapping(RequestMapping requestMapping, PutMapping putMapping, Method method, String key) {
        if(requestMapping.value().length == 0){
            RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths(putMapping.value())
                    .produces(putMapping.produces())
                    .params(putMapping.params())
                    .methods(RequestMethod.PUT)
                    .headers(putMapping.headers())
                    .consumes(putMapping.consumes())
                    .build();
            registryHandlerMapping(requestMappingInfo,method,key);
        }else{
            for (String s : requestMapping.value()) {
                RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths(builderPath(s,putMapping.value()))
                        .produces(putMapping.produces())
                        .params(putMapping.params())
                        .methods(RequestMethod.PUT)
                        .headers(putMapping.headers())
                        .consumes(putMapping.consumes())
                        .build();
                registryHandlerMapping(requestMappingInfo,method,key);
            }
        }
    }

    private void registryPostHandlerMapping(RequestMapping requestMapping, PostMapping postMapping, Method method, String key) {
        if(requestMapping.value().length == 0){
            RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths(postMapping.value())
                    .produces(postMapping.produces())
                    .params(postMapping.params())
                    .methods(RequestMethod.POST)
                    .headers(postMapping.headers())
                    .consumes(postMapping.consumes())
                    .build();
            registryHandlerMapping(requestMappingInfo,method,key);
        }else{
            for (String s : requestMapping.value()) {
                RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths(builderPath(s,postMapping.value()))
                        .produces(postMapping.produces())
                        .params(postMapping.params())
                        .methods(RequestMethod.POST)
                        .headers(postMapping.headers())
                        .consumes(postMapping.consumes())
                        .build();
                registryHandlerMapping(requestMappingInfo,method,key);
            }
        }
    }

    private void registryGetHandlerMapping(RequestMapping requestMapping, GetMapping getMapping, Method method, String key) {
        if(requestMapping.value().length == 0){
            RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths(getMapping.value())
                    .produces(getMapping.produces())
                    .params(getMapping.params())
                    .methods(RequestMethod.GET)
                    .headers(getMapping.headers())
                    .consumes(getMapping.consumes())
                    .build();
            registryHandlerMapping(requestMappingInfo,method,key);
        }else{
            for (String s : requestMapping.value()) {
                RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths(builderPath(s,getMapping.value()))
                        .produces(getMapping.produces())
                        .params(getMapping.params())
                        .methods(RequestMethod.GET)
                        .headers(getMapping.headers())
                        .consumes(getMapping.consumes())
                        .build();
                registryHandlerMapping(requestMappingInfo,method,key);
            }
        }

    }

    private String[] builderPath(String s, String[] value) {
        List<String> list = new ArrayList<>();
        for (String path : value) {
            list.add(parsePath(s,path));
        }
        return list.toArray(new String[list.size()]);
    }

    private String parsePath(String s, String path) {
        if(s.endsWith("/") && path.startsWith("/")){
            return s + path.substring(1);
        }else if(s.endsWith("/") || path.startsWith("/")){
            return s + path;
        }else{
            return s + "/" + path;
        }
    }

    /**
     * 注册requestMapping
     * @param requestMappingInfo
     * @param method
     * @param key
     */
    private void registryHandlerMapping(RequestMappingInfo requestMappingInfo, Method method,String key) {
        System.out.println("plugin registry handler mapping url is ===> " + requestMappingInfo);
        requestMappingHandlerMapping.registerMapping(requestMappingInfo,key,method);
    }


    /**
     * 初始化组件
     * @param clazz
     * @throws Exception
     */
    private void initComponent(String clazz) throws Exception {
        Class<?> c = Class.forName(clazz);
        Component annotation = AnnotationUtils.findAnnotation(c, Component.class);
        if(annotation != null){
            String cName = c.getName().substring(c.getName().lastIndexOf(".") + 1, c.getName().length());
            String beanName = StringUtils.isNotBlank(annotation.value()) ? annotation.value() : cName.substring(0,1).toLowerCase() + cName.substring(1,cName.length());
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(c);
            AbstractBeanDefinition singleton = beanDefinitionBuilder.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE)
                    .setScope("singleton")
                    .getRawBeanDefinition();
            singleton.setAutowireCandidate(true);
            applicationContext.registerBeanDefinition(beanName,singleton);
            beans.put(beanName,c);
        }
    }

    @Override
    public void stop() {
        this.isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

}
