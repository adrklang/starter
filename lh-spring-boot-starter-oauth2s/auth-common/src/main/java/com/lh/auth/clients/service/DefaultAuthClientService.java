package com.lh.auth.clients.service;

import com.lh.auth.clients.common.api.BaseOauthClientDetailsApi;
import com.lh.auth.clients.common.details.BaseAuthClientProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultAuthClientService extends AbstractAuthClientDetailsService<BaseOauthClientDetailsApi> {

    @Autowired
    private BaseAuthClientProperties baseAuthClientProperties;

    @Override
    public BaseOauthClientDetailsApi getClient(String clientId) {
        List<? extends BaseOauthClientDetailsApi> clients = baseAuthClientProperties.getClients();
        Map<String, BaseOauthClientDetailsApi> clientDetailsMap = clients.stream().collect(Collectors.toMap(BaseOauthClientDetailsApi::getClientId, client -> client));
        if(!clientDetailsMap.containsKey(clientId)){
            throw new NullPointerException();
        }
        return clientDetailsMap.get(clientId);
    }
}
