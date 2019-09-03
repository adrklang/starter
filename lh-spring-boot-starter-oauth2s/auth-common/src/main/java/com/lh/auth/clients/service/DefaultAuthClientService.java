package com.lh.auth.clients.service;

import com.lh.auth.clients.common.api.BaseOauthClientDetailsApi;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 加载auth客户端的实现类,用户不需要注入此bean对象，如果要扩展，请继承AbstractAuthClientDetailsService抽象类
 */
public class DefaultAuthClientService extends AbstractAuthClientDetailsService<BaseOauthClientDetailsApi> {

    @Autowired
    private LoadAuthClientService loadAuthClientService;

    @Override
    public BaseOauthClientDetailsApi getClient(String clientId) {
       return loadAuthClientService.loadByClientId(clientId);
    }
}
