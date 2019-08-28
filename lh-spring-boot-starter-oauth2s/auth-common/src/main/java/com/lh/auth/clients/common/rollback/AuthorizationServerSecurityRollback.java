package com.lh.auth.clients.common.rollback;

import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

public interface AuthorizationServerSecurityRollback {
    void rollback(AuthorizationServerSecurityConfigurer security) throws Exception;
}
