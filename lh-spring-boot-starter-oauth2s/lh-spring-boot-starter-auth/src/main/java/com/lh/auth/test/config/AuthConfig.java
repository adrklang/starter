package com.lh.auth.test.config;

import com.lh.auth.clients.common.api.ClientDetailsServiceApi;
import com.lh.auth.clients.service.DefaultAuthClientService;
import com.lh.auth.clients.service.LoadAuthClientService;
import com.lh.auth.clients.token.common.TokenParseConverter;
import com.lh.auth.clients.token.jwt.JwtTokenParseConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {


   /* @Bean
    public ClientDetailsServiceApi defaultAuthClientService(){
        return new DefaultAuthClientService();
    }
*/
   /* @Bean
    public TokenParseConverter tokenParseConverter(){
        return new JwtTokenParseConverter();
    }*/

}
