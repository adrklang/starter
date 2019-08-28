package com.lh.auth.clients.common.details;

import lombok.Data;

import java.util.List;

@Data
public class BaseAuthClientProperties {
    private List<BaseOauthClientDetails> clients;
}
