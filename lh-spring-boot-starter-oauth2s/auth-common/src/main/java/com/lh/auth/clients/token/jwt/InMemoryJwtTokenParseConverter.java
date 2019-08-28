package com.lh.auth.clients.token.jwt;

import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * jwt转换
 *
 */
public class InMemoryJwtTokenParseConverter extends AbstractJwtTokenParseConverter<TokenStore>{

    @Override
    public TokenStore getTokenStore() {
        return new InMemoryTokenStore();
    }
}
