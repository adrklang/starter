package com.lh.auth.clients.service;

import com.lh.auth.clients.common.api.BaseOauthClientDetailsApi;

public interface IJdbcClientService {
    BaseOauthClientDetailsApi loadBaseOauthClientDetailsApi(String clientId);
}
