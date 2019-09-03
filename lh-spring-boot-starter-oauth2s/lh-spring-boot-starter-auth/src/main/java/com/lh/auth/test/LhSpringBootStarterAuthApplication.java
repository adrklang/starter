package com.lh.auth.test;

import com.lh.auth.clients.configuration.EnableAuthorizationServer;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@SpringBootApplication
@EnableAuthorizationServer//开启授权中心自动配置
public class LhSpringBootStarterAuthApplication implements ApplicationContextAware {

    public static void main(String[] args) {
        SpringApplication.run(LhSpringBootStarterAuthApplication.class, args);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
