package com.lh.auto.limit.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.concurrent.TimeUnit;

@Data
@Accessors(chain = true)
public class RateLimiter {
    private Long time;
    private com.google.common.util.concurrent.RateLimiter rateLimiter;

    public RateLimiter(long currentTimeMillis, com.google.common.util.concurrent.RateLimiter rateLimiter) {
        this.time = currentTimeMillis;
        this.rateLimiter = rateLimiter;
    }

    public static RateLimiter create(long count, long seconds, TimeUnit seconds1) {
        com.google.common.util.concurrent.RateLimiter gRateLimiter = com.google.common.util.concurrent.RateLimiter.create(count, seconds, seconds1);
        return new RateLimiter(System.currentTimeMillis(),gRateLimiter);
    }

    public Boolean tryAcquire(){
        return rateLimiter.tryAcquire();
    }
}
