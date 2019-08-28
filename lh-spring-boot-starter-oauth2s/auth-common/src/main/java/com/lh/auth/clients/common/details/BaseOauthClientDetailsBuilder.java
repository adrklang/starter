package com.lh.auth.clients.common.details;

import com.lh.auth.clients.common.api.BaseOauthClientDetailsApi;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.util.*;

public class BaseOauthClientDetailsBuilder {

        private final String clientId;

        private Collection<String> authorizedGrantTypes = new LinkedHashSet<String>();

        private Collection<String> authorities = new LinkedHashSet<String>();

        private Integer accessTokenValiditySeconds;

        private Integer refreshTokenValiditySeconds;

        private Collection<String> scopes = new LinkedHashSet<String>();

        private Collection<String> autoApproveScopes = new HashSet<String>();

        private String secret;

        private Set<String> registeredRedirectUris = new HashSet<String>();

        private Set<String> resourceIds = new HashSet<String>();

        private boolean autoApprove;

        private Map<String, Object> additionalInformation = new LinkedHashMap<String, Object>();


        public static ClientDetails build(BaseOauthClientDetailsApi baseOauthClientDetails, PasswordEncoder passwordEncoder){
            BaseClientDetails result = new BaseClientDetails();
            result.setClientId(baseOauthClientDetails.getClientId());
            result.setAuthorizedGrantTypes(baseOauthClientDetails.getAuthorizedGrantTypes());
            result.setAccessTokenValiditySeconds(baseOauthClientDetails.getAccessTokenValiditySeconds());
            result.setRefreshTokenValiditySeconds(baseOauthClientDetails.getRefreshTokenValiditySeconds());
            result.setRegisteredRedirectUri(baseOauthClientDetails.getRedirectUris());
            result.setClientSecret(passwordEncoder.encode(baseOauthClientDetails.getSecret()));
            result.setScope(baseOauthClientDetails.getScopes());
            result.setAuthorities(AuthorityUtils.createAuthorityList(baseOauthClientDetails.getAuthorities().toArray(new String[baseOauthClientDetails.getAuthorities().size()])));
            result.setResourceIds(baseOauthClientDetails.getResourceIds());
            result.setAdditionalInformation(baseOauthClientDetails.getAdditionalInformationMap());
            if (baseOauthClientDetails.getAutoApprove()) {
                result.setAutoApproveScopes(baseOauthClientDetails.getAutoApproves());
            }
            else {
                result.setAutoApproveScopes(baseOauthClientDetails.getAutoApproves());
            }
            return result;
        }

        public ClientDetails build() {
            BaseClientDetails result = new BaseClientDetails();
            result.setClientId(clientId);
            result.setAuthorizedGrantTypes(authorizedGrantTypes);
            result.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
            result.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
            result.setRegisteredRedirectUri(registeredRedirectUris);
            result.setClientSecret(secret);
            result.setScope(scopes);
            result.setAuthorities(AuthorityUtils.createAuthorityList(authorities.toArray(new String[authorities.size()])));
            result.setResourceIds(resourceIds);
            result.setAdditionalInformation(additionalInformation);
            if (autoApprove) {
                result.setAutoApproveScopes(scopes);
            }
            else {
                result.setAutoApproveScopes(autoApproveScopes);
            }
            return result;
        }

        public BaseOauthClientDetailsBuilder resourceIds(String... resourceIds) {
            for (String resourceId : resourceIds) {
                this.resourceIds.add(resourceId);
            }
            return this;
        }

        public BaseOauthClientDetailsBuilder redirectUris(String... registeredRedirectUris) {
            for (String redirectUri : registeredRedirectUris) {
                this.registeredRedirectUris.add(redirectUri);
            }
            return this;
        }

        public BaseOauthClientDetailsBuilder authorizedGrantTypes(String... authorizedGrantTypes) {
            for (String grant : authorizedGrantTypes) {
                this.authorizedGrantTypes.add(grant);
            }
            return this;
        }

        public BaseOauthClientDetailsBuilder accessTokenValiditySeconds(int accessTokenValiditySeconds) {
            this.accessTokenValiditySeconds = accessTokenValiditySeconds;
            return this;
        }

        public BaseOauthClientDetailsBuilder refreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
            this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
            return this;
        }

        public BaseOauthClientDetailsBuilder secret(String secret) {
            this.secret = secret;
            return this;
        }

        public BaseOauthClientDetailsBuilder scopes(String... scopes) {
            for (String scope : scopes) {
                this.scopes.add(scope);
            }
            return this;
        }

        public BaseOauthClientDetailsBuilder authorities(String... authorities) {
            for (String authority : authorities) {
                this.authorities.add(authority);
            }
            return this;
        }

        public BaseOauthClientDetailsBuilder autoApprove(boolean autoApprove) {
            this.autoApprove = autoApprove;
            return this;
        }

        public BaseOauthClientDetailsBuilder autoApprove(String... scopes) {
            for (String scope : scopes) {
                this.autoApproveScopes.add(scope);
            }
            return this;
        }

        public BaseOauthClientDetailsBuilder additionalInformation(Map<String, ?> map) {
            this.additionalInformation.putAll(map);
            return this;
        }

        public BaseOauthClientDetailsBuilder additionalInformation(String... pairs) {
            for (String pair : pairs) {
                String separator = ":";
                if (!pair.contains(separator) && pair.contains("=")) {
                    separator = "=";
                }
                int index = pair.indexOf(separator);
                String key = pair.substring(0, index > 0 ? index : pair.length());
                String value = index > 0 ? pair.substring(index+1) : null;
                this.additionalInformation.put(key, (Object) value);
            }
            return this;
        }


        public BaseOauthClientDetailsBuilder(String clientId) {
            this.clientId = clientId;
        }

}
