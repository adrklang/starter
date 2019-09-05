package com.lh.auto.limit.service;

import com.lh.auto.limit.annotation.ResourceLimit;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 自定义限流规则，只需要实现此接口，将此接口的实现类添加到容器即可
 */
public interface ResourceLimitService {
    /**
     * 判断限流规则是否存在
     * @param resourceLimit
     * @return
     */
    Boolean isExist(ResourceLimit resourceLimit);

    /**
     * 执行限流
     * @param resourceLimit
     * @return
     */
    boolean proceedingRateLimit(ResourceLimit resourceLimit);

    /**
     * 限流回调
     * @param resourceLimit
     * @param joinPoint
     * @return
     * @throws Exception
     */
    Object rateLimitFallback(ResourceLimit resourceLimit, ProceedingJoinPoint joinPoint) throws Exception ;
}
