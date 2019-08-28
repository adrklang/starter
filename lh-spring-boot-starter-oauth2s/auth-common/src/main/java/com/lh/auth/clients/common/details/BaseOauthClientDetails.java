package com.lh.auth.clients.common.details;

import com.lh.auth.clients.common.api.BaseOauthClientDetailsApi;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Data
@Accessors(chain = true)
public class BaseOauthClientDetails implements BaseOauthClientDetailsApi {
    /**
     * clientId
     */
    private String clientId;

    /**
     * secret
     */
    private String secret;
    /**
     * 自动批准
     */
    private Boolean autoApprove = false;
    /**
     * 访问token有效性
     */
    private Integer accessTokenValiditySeconds;
    /**
     * 刷新token有效性
     */
    private Integer refreshTokenValiditySeconds;
    /**
     * 授权类型,password , authorization_code , refresh_token , implicit , client_credentials
     * 多个之间用 , 隔开
     */
    private Set<String> authorizedGrantTypes = new LinkedHashSet<String>();;
    /**
     * 重定向地址，多个之间用 , 隔开
     */
    private Set<String> redirectUris = new LinkedHashSet<String>();;

    /**
     * 授权范围 多个之间用 , 隔开
     */
    private Set<String> scopes = new LinkedHashSet<String>();

    /**
     *  权限 多个之间用 , 隔开
     */
    private Set<String> authorities = new LinkedHashSet<String>();
    /**
     * 资源Id 多个之间用 , 隔开
     */
    private Set<String> resourceIds = new LinkedHashSet<String>();
    /**
     * 附加信息
     */
    private String additionalInformation;


    private Map<String,Object> additionalInformationMap = new HashMap<>();

    /**
     * 自动批准指定的scopes 多个之间用 , 隔开
     */
    private Set<String> autoApproves = new LinkedHashSet<String>();
}
