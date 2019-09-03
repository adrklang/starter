package com.lh.auth.clients.configuration;

import com.lh.auth.clients.common.api.ClientDetailsServiceApi;
import com.lh.auth.clients.common.details.BaseAuthClientProperties;
import com.lh.auth.clients.service.DefaultAuthClientService;
import com.lh.auth.clients.service.DefaultLoadAuthClientService;
import com.lh.auth.clients.service.LoadAuthClientService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

public class ClientDetailsServiceAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "lh.auto.auth")
    @ConditionalOnMissingBean
    public BaseAuthClientProperties baseAuthClientProperties(){
        return new BaseAuthClientProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public ClientDetailsServiceApi defaultAuthClientService(){
        return new DefaultAuthClientService();
    }

    @Bean
    @ConditionalOnMissingBean
    public LoadAuthClientService loadAuthClientService(){
        return new DefaultLoadAuthClientService();
    }
}
