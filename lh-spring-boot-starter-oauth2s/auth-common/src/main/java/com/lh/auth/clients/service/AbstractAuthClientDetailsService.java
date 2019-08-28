package com.lh.auth.clients.service;

import com.lh.auth.clients.common.api.ClientDetailsServiceApi;
import com.lh.auth.clients.common.details.BaseOauthClientDetailsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

/**
 *
 * @param <T extends BaseOauthClientDetailsApi>
 */
public abstract class AbstractAuthClientDetailsService<T> implements ClientDetailsServiceApi {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return BaseOauthClientDetailsBuilder.build(getClient(clientId),passwordEncoder);
    }
}
