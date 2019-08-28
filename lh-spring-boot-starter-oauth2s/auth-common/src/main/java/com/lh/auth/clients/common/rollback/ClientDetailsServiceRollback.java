package com.lh.auth.clients.common.rollback;

import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;

public interface ClientDetailsServiceRollback {
    void rollback(ClientDetailsServiceConfigurer clients) throws Exception;
}
