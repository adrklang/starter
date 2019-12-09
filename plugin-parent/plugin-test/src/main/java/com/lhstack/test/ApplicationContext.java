package com.lhstack.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping
public class ApplicationContext {

    @Autowired
    private org.springframework.context.ApplicationContext applicationContext;


    @GetMapping
    public Object msg() throws ClassNotFoundException {
        Object bean = applicationContext.getBean(Class.forName("com.lhstack.controller.TestController"));
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        return beanDefinitionNames;
    }
    public static void main(String[] args) {
        SpringApplication.run(ApplicationContext.class,args);
    }
}
