package com.lh.auth.clients.common.api;

import java.util.Map;
import java.util.Set;

public interface BaseOauthClientDetailsApi {
    String getClientId();

    Boolean getAutoApprove();

    Integer getAccessTokenValiditySeconds();

    Integer getRefreshTokenValiditySeconds();

    Set<String> getAuthorizedGrantTypes();

    Set<String> getRedirectUris();

    Set<String> getScopes();

    Set<String> getAuthorities();

    Set<String> getResourceIds();

    String getAdditionalInformation();

    Map<String, Object> getAdditionalInformationMap();

    Set<String> getAutoApproves();

    String getSecret();
}
