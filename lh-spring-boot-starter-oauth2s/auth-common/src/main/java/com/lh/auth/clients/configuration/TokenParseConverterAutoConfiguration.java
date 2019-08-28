package com.lh.auth.clients.configuration;

import com.lh.auth.clients.token.common.TokenParseConverter;
import com.lh.auth.clients.token.jwt.JwtProperties;
import com.lh.auth.clients.token.memory.InMemoryTokenParseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;


public class TokenParseConverterAutoConfiguration {

    @Autowired
    private TokenParseConverter tokenParseConverter;


    /**
     * 默认使用基于内存的TokenParseConverter,如要自定义，实现AbstractTokenParseConverter 抽象类并注入到容器即可
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public TokenParseConverter tokenParseConverter(){
        return new InMemoryTokenParseConverter();
    }

    @Bean
    @ConfigurationProperties(prefix = "lh.auto.auth.jwt")
    public JwtProperties jwtProperties(){
        return new JwtProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenStore tokenStore(){
        return tokenParseConverter.getTokenStore();
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenEnhancer tokenEnhancer(){
        return tokenParseConverter.getTokenEnhancer();
    }

    @Bean
    @ConditionalOnMissingBean
    public AccessTokenConverter accessTokenConverter(){
        return tokenParseConverter.getAccessTokenConverter();
    }
}
