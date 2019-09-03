package com.lh.auto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class LhSpringBootStarterResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LhSpringBootStarterResourceApplication.class, args);
    }
    @GetMapping("info")
    public Object getName(@AuthenticationPrincipal Authentication authentication){
        return authentication.getAuthorities();
    }
}
