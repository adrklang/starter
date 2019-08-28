package com.lh.auth.clients.token.jwt;

import com.lh.auth.clients.token.common.AbstractTokenParseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * 封装jwt重复的操作
 */
public abstract class AbstractJwtTokenParseConverter<T extends TokenStore> extends AbstractTokenParseConverter<T,JwtAccessTokenConverter, TokenEnhancer> {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public TokenEnhancer getTokenEnhancer() {
        return provideAccessTokenConverter();
    }

    @Override
    public abstract T getTokenStore();

    @Override
    protected JwtAccessTokenConverter provideAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        if(jwtProperties.isKeyPair()){
            jwtAccessTokenConverter.setKeyPair(jwtProperties.getKeyPair());
        }else if(jwtProperties.isSigningKey()){
            jwtAccessTokenConverter.setSigningKey(jwtProperties.getSigningKey());
        }
        return jwtAccessTokenConverter;
    }

}
