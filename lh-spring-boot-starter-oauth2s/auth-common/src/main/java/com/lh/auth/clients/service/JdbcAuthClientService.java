package com.lh.auth.clients.service;

import com.lh.auth.clients.common.api.BaseOauthClientDetailsApi;
import org.springframework.beans.factory.annotation.Autowired;


public class JdbcAuthClientService extends AbstractAuthClientDetailsService<BaseOauthClientDetailsApi> {

    @Autowired
    private IJdbcClientService jdbcClientService;

    @Override
    public BaseOauthClientDetailsApi getClient(String clientId) {
        return jdbcClientService.loadBaseOauthClientDetailsApi(clientId);
    }
}
