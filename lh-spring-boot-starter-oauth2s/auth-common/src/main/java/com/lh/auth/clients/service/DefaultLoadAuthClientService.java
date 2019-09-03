package com.lh.auth.clients.service;

import com.lh.auth.clients.common.api.BaseOauthClientDetailsApi;
import com.lh.auth.clients.common.details.BaseAuthClientProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 从配置文件里面加载客户端信息到auth中心，是LoadAuthClientService默认实现类
 */
public class DefaultLoadAuthClientService implements LoadAuthClientService {
    @Autowired
    private BaseAuthClientProperties baseAuthClientProperties;
    @Override
    public BaseOauthClientDetailsApi loadByClientId(String clientId) {
        List<? extends BaseOauthClientDetailsApi> clients = baseAuthClientProperties.getClients();
        Map<String, BaseOauthClientDetailsApi> clientDetailsMap = clients.stream().collect(Collectors.toMap(BaseOauthClientDetailsApi::getClientId, client -> client));
        if(!clientDetailsMap.containsKey(clientId)){
            throw new NullPointerException();
        }
        return clientDetailsMap.get(clientId);
    }
}
