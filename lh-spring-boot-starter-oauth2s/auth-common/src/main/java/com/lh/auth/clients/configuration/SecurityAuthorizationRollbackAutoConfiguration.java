package com.lh.auth.clients.configuration;

import com.lh.auth.clients.common.api.ClientDetailsServiceApi;
import com.lh.auth.clients.common.rollback.AuthorizationServerEndpointsRollback;
import com.lh.auth.clients.common.rollback.AuthorizationServerSecurityRollback;
import com.lh.auth.clients.common.rollback.ClientDetailsServiceRollback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

public class SecurityAuthorizationRollbackAutoConfiguration {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AccessTokenConverter accessTokenConverter;

    @Autowired
    private TokenEnhancer tokenEnhancer;

    @Autowired
    private ClientDetailsServiceApi clientDetailsServiceApi;

    /**
     * 默认 AuthorizationServerSecurity   security回调函数，如要自定义，注册AuthorizationServerEndpointsRollback接口的实现类到容器即可
     * @return AuthorizationServerEndpointsRollback
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthorizationServerSecurityRollback authorizationServerSecurityRollback(){
        return security -> {
            security
                    .tokenKeyAccess("permitAll()")
                    .checkTokenAccess("isAuthenticated()")
                    .allowFormAuthenticationForClients();
        };
    }

    /**
     * 默认AuthorizationServerEndpoints  endpoints回调函数，如要自定义，注册AuthorizationServerEndpointsRollback接口的实现类到容器即可
     * @return AuthorizationServerEndpointsRollback
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthorizationServerEndpointsRollback authorizationServerEndpointsRollback(){
        return endpoints -> {
            endpoints
                    .tokenStore(tokenStore)
                    .accessTokenConverter(accessTokenConverter)
                    .tokenEnhancer(tokenEnhancer)
                    .authenticationManager(authenticationManager);
        };
    }

    /**
     * 默认ClientDetailsService  clients回调函数，如要自定义，注册AuthorizationServerEndpointsRollback接口的实现类到容器即可
     * @return AuthorizationServerEndpointsRollback
     */
    @Bean
    @ConditionalOnMissingBean
    public ClientDetailsServiceRollback clientDetailsServiceRollback(){
        return clients -> {
            clients.withClientDetails(clientDetailsServiceApi);
        };
    }

}
