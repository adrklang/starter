package com.lh.auto.limit.fallback;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;

@Data
@Builder
public class FallbackPojoInfo {

    /**
     * 被限流的方法
     */
    private Method method;

    /**
     * 被限流的对象
     */
    private Object target;

    /**
     * 形参
     */
    private Object[] parameters;

}
