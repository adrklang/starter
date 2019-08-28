package com.lh.auth.clients.common.rollback;

import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

public interface AuthorizationServerEndpointsRollback {
    void rollback(AuthorizationServerEndpointsConfigurer endpoints) throws Exception;
}
