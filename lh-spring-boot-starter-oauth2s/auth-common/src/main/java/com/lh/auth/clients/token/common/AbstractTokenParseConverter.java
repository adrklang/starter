package com.lh.auth.clients.token.common;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTokenParseConverter<T extends TokenStore,A extends AccessTokenConverter,E extends TokenEnhancer> implements TokenParseConverter {


    @Override
    public AccessTokenConverter getAccessTokenConverter() {
        return new AccessTokenConverterWapper(provideAccessTokenConverter());
    }

    protected abstract A provideAccessTokenConverter();


    private static class AccessTokenConverterWapper implements AccessTokenConverter{
        private AccessTokenConverter accessTokenConverter;
        public AccessTokenConverterWapper(AccessTokenConverter accessTokenConverter) {
            this.accessTokenConverter = accessTokenConverter;
        }

        @Override
        public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
            DefaultOAuth2AccessToken oAuth2AccessToken = (DefaultOAuth2AccessToken) token;
            Map<String,Object> map = new HashMap<>();
            map.put("author","LiHang");
            map.put("github","https://github.com/adrklang");
            map.put("gitee","https://gitee.com/myprofile");
            map.put("website","http://www.lhstack.com");
            oAuth2AccessToken.setAdditionalInformation(map);
            return accessTokenConverter.convertAccessToken(token,authentication);
        }

        @Override
        public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
            return accessTokenConverter.extractAccessToken(value,map);
        }

        @Override
        public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
            return accessTokenConverter.extractAuthentication(map);
        }
    }
}
