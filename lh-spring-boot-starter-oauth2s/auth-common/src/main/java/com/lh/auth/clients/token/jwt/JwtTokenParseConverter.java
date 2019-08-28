package com.lh.auth.clients.token.jwt;

import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * jwt转换
 *
 */
public class JwtTokenParseConverter extends AbstractJwtTokenParseConverter<JwtTokenStore>{

    @Override
    public JwtTokenStore getTokenStore() {
        return new JwtTokenStore(provideAccessTokenConverter());
    }

}
