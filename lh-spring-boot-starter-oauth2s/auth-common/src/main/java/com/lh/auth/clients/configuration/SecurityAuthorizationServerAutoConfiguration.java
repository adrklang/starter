package com.lh.auth.clients.configuration;

import com.lh.auth.clients.common.rollback.AuthorizationServerEndpointsRollback;
import com.lh.auth.clients.common.rollback.AuthorizationServerSecurityRollback;
import com.lh.auth.clients.common.rollback.ClientDetailsServiceRollback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@EnableAuthorizationServer
public class SecurityAuthorizationServerAutoConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthorizationServerSecurityRollback authorizationServerSecurityRollback;

    @Autowired
    private AuthorizationServerEndpointsRollback authorizationServerEndpointsRollback;

    @Autowired
    private ClientDetailsServiceRollback clientDetailsServiceRollback;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        authorizationServerSecurityRollback.rollback(security);
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        authorizationServerEndpointsRollback.rollback(endpoints);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clientDetailsServiceRollback.rollback(clients);
    }
}
