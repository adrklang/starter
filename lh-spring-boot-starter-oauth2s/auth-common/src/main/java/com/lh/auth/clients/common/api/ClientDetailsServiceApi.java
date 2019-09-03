package com.lh.auth.clients.common.api;

import org.springframework.security.oauth2.provider.ClientDetailsService;
/*
 * 客户端服务api
 */
public interface ClientDetailsServiceApi<T extends BaseOauthClientDetailsApi> extends ClientDetailsService {
    T getClient(String clientId);
}
