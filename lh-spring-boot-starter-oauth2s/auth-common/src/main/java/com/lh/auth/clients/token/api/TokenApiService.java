package com.lh.auth.clients.token.api;

import com.lh.auth.clients.token.common.TokenParseConverter;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public interface TokenApiService {
    default TokenParseConverter getTokenParseConverter(){
        return null;
    }
    default TokenEnhancer getTokenEnhancer(){
        return null;
    }
    default AccessTokenConverter getAccessTokenConverter(){
        return null;
    }
}
