package com.lh.auth.clients.token.memory;

import com.lh.auth.clients.token.common.AbstractTokenParseConverter;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

public class InMemoryTokenParseConverter extends AbstractTokenParseConverter<TokenStore, AccessTokenConverter, TokenEnhancer> {
    @Override
    public TokenStore getTokenStore() {
        return new InMemoryTokenStore();
    }

    @Override
    public TokenEnhancer getTokenEnhancer() {
        return (accessToken, authentication) -> {
            return accessToken;
        };
    }
    @Override
    protected AccessTokenConverter provideAccessTokenConverter() {
        return new DefaultAccessTokenConverter();
    }

}
