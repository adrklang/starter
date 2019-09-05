package com.lh.auto.limit.fallback;

import com.lh.auto.limit.exception.LimitException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultFallbackFactory {
    public static void fallback(FallbackPojoInfo fallbackPojoInfo){
        log.error("限流回调执行: method is {},parameters is {},targe is {}",fallbackPojoInfo.getMethod().getName(),fallbackPojoInfo.getParameters(),fallbackPojoInfo.getTarget());
        throw new LimitException("方法限流 --- executor ...");
    }
}
