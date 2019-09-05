package com.lh.auto.limit.test;

import com.lh.auto.limit.annotation.EnableRateLimit;
import com.lh.auto.limit.annotation.ResourceLimit;
import com.lh.auto.limit.fallback.FallbackPojoInfo;
import com.lh.auto.limit.model.LimitService;
import com.lh.auto.limit.model.LimitType;
import com.lh.auto.limit.test.fallback.FallbackFactory;
import com.lh.auto.limit.test.fallback.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@EnableRateLimit
@RestController
public class MicroserviceMyblogLimitTestApplication  {
    /**
     * 每5s填充一枚令牌，初始化令牌桶100
     * @return
     */
    @GetMapping("hello")
    @ResourceLimit(key = "hello",seconds = 5, capacity = 10)
    public String hello(){
        return "Hello World";
    }
    @Autowired
    private TestService testService;

    @GetMapping("hello/ip/{message}")
    @ResourceLimit(key="message",seconds = 15, capacity = 5,fallbackFactory = FallbackFactory.class,method = "message",type = LimitType.IP,useLimitService = LimitService.JDK,secondsAddCount = 2)
    public String ipMessage(@PathVariable("message") String message){
        return "hello " + message;
    }
    @GetMapping("hello/session/{message}")
    @ResourceLimit(key="message",seconds = 1, capacity = 5,fallbackFactory = FallbackFactory.class,method = "message",type = LimitType.SESSION,useLimitService = LimitService.JDK,secondsAddCount = 2)
    public String sessionMessage(@PathVariable("message") String message, HttpServletRequest request){
        return "hello " + message;
    }
    @GetMapping("hello/{message}")
    @ResourceLimit(key="message",seconds = 1, capacity = 5,type = LimitType.NONE,useLimitService = LimitService.JDK,secondsAddCount = 2)
    public String message(@PathVariable("message") String message, HttpServletRequest request){
        return "hello " + message;
    }

    @GetMapping("test/{message}")
    @ResourceLimit(key="message",useLimitService = LimitService.CONSUMER,limitServiceBeanName = "MyResourceServicImpl")
    public String my(@PathVariable("message") String message, HttpServletRequest request){
        return "hello " + message;
    }
    public static void main(String[] args) {
        SpringApplication.run(MicroserviceMyblogLimitTestApplication.class, args);
    }

}
