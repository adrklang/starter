package com.lh.auto.limit.fallback;

import org.aspectj.lang.ProceedingJoinPoint;

public interface FallbackFactoryInvokeExcutor {
    /**
     *
     * @param clazz 工厂class字节码
     * @param methodName 限流回调方法名
     * @param joinPoint
     * @return
     */
    Object invoke(Class clazz, String methodName, ProceedingJoinPoint joinPoint) throws Exception;
}
