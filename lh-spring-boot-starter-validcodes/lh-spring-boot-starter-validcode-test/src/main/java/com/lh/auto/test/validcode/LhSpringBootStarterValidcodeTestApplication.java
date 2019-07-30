package com.lh.auto.test.validcode;

import com.lh.auto.validcode.annotation.EnableValidCode;
import com.lh.auto.validcode.service.ValidCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SpringBootApplication
@EnableValidCode
@RestController
public class LhSpringBootStarterValidcodeTestApplication {

    @Autowired
    private ValidCodeService validCodeService;
    @GetMapping("code")
    public void valid(HttpServletResponse response) throws IOException {
        response.setHeader("Pragma", "no-cache");    //HTTP   1.0
        response.setHeader("Cache-Control", "no-cache");//HTTP   1.1
        response.setDateHeader("Expires", 0);      //在代理服务器端防止缓冲
        response.setContentType("image/gif");
        validCodeService.createValidCode(response.getOutputStream());
    }

    public static void main(String[] args) {
        SpringApplication.run(LhSpringBootStarterValidcodeTestApplication.class, args);
    }

}
