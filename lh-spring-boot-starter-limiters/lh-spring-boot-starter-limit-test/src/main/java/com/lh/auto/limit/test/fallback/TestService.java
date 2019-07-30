package com.lh.auto.limit.test.fallback;

import com.lh.auto.limit.annotation.ResourceLimit;
import com.lh.auto.limit.model.LimitService;
import com.lh.auto.limit.model.LimitType;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @ResourceLimit(key="message",seconds = 1, capacity = 5,fallbackFactory = FallbackFactory.class,method = "message",type = LimitType.IP,useLimitService = LimitService.JDK,secondsAddCount = 5)
    public String ipMessage(String message){
        return "hello " + message;
    }
}
