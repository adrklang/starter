package com.lh.auth.clients.service;

import com.lh.auth.clients.common.api.BaseOauthClientDetailsApi;

/**
 * 可自定义加载方式，实现此接口即可，默认从配置文件加载
 */
public interface LoadAuthClientService {
    BaseOauthClientDetailsApi loadByClientId(String clientId);
}
