package com.lh.auth.clients.token.common;

import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

public interface TokenParseConverter<T extends TokenStore,A extends AccessTokenConverter,E extends TokenEnhancer> {
    TokenStore getTokenStore();
    AccessTokenConverter getAccessTokenConverter();
    TokenEnhancer getTokenEnhancer();
}
