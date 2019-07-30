package com.lh.auto.limit.test.fallback;

import com.lh.auto.limit.annotation.ResourceLimit;
import com.lh.auto.limit.service.ResourceLimitService;
import org.springframework.stereotype.Component;

@Component("MyResourceServicImpl")
public class MyResourceServicImpl implements ResourceLimitService {
    @Override
    public Boolean isExist(ResourceLimit resourceLimit) {
        System.out.println("自定义限流");
        return true;
    }

    @Override
    public boolean proceedingRateLimit(ResourceLimit resourceLimit) {
        return false;
    }

    @Override
    public Object rateLimitFallback(ResourceLimit resourceLimit, Object... args) throws Exception {
        return null;
    }
}
