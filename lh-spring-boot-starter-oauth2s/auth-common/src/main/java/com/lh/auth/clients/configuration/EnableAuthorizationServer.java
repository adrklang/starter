package com.lh.auth.clients.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SecurityAuthorizationServerAutoConfiguration.class})
public @interface EnableAuthorizationServer {
}
