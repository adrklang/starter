package com.lh.auto.limit.service;

import com.lh.auto.limit.annotation.ResourceLimit;

/**
 * 自定义限流规则，只需要实现此接口，将此接口的实现类添加到容器即可
 */
public interface ResourceLimitService {
    Boolean isExist(ResourceLimit resourceLimit);
    boolean proceedingRateLimit(ResourceLimit resourceLimit);
    Object rateLimitFallback(ResourceLimit resourceLimit,Object ... args) throws Exception ;
}
