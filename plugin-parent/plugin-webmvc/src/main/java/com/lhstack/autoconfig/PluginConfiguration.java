package com.lhstack.autoconfig;

import com.lhstack.spring.context.SpringPluginContextApplication;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PluginConfiguration extends SpringPluginContextApplication {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @SneakyThrows
    @Override
    public void start() {
        super.start();
        initController();
    }

    private void initController() {
        Set<Map.Entry<String, Class>> entries = getBeans().entrySet();
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
        if(value.length == 0){
            return new String[]{s};
        }
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


    @Override
    public void stop() {
        this.isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

}
