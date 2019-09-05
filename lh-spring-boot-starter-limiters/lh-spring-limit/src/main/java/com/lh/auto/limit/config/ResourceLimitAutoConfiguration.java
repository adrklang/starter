package com.lh.auto.limit.config;


import com.lh.auto.limit.annotation.ResourceLimit;
import com.lh.auto.limit.fallback.DefaultFallbackFactoryInvokeExecutor;
import com.lh.auto.limit.fallback.FallbackFactoryInvokeExcutor;
import com.lh.auto.limit.service.GuavaRateLimitServiceImpl;
import com.lh.auto.limit.service.JDKRateLimitServiceImpl;
import com.lh.auto.limit.service.ResourceLimitService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.ParseException;

import java.util.concurrent.locks.ReentrantReadWriteLock;

@Aspect
public class ResourceLimitAutoConfiguration {

    @Autowired
    @Qualifier("GuavaRateLimitServiceImpl")
    private ResourceLimitService rateResourceLimitService;

    private static ReentrantReadWriteLock reentrantLock = new ReentrantReadWriteLock();

    @Autowired
    @Qualifier("JDKRateLimitServiceImpl")
    private ResourceLimitService jdkResourceLimitService;

    @Bean("JDKRateLimitServiceImpl")
    public ResourceLimitService jdkResourceLimitService(){
        return new JDKRateLimitServiceImpl();
    }
    //默认限流规则
    @Bean("GuavaRateLimitServiceImpl")
    public ResourceLimitService rateResourceLimitService(){
        return new GuavaRateLimitServiceImpl();
    }


    @Bean
    public FallbackFactoryInvokeExcutor fallbackFactoryInvokeExcutor(){
        return new DefaultFallbackFactoryInvokeExecutor();
    }

    @Bean
    public ApplicationUtils applicationUtils(){
        return new ApplicationUtils();
    }

    @Around("execution(public * *(..)) && @annotation(resourceLimit)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, ResourceLimit resourceLimit) throws Throwable {
        switch (resourceLimit.useLimitService()){
            case RATE: return proceedingRateLimit(proceedingJoinPoint,resourceLimit,rateResourceLimitService);
            case JDK: return proceedingRateLimit(proceedingJoinPoint,resourceLimit,jdkResourceLimitService);
            case CONSUMER: return proceedingConsumerRateLimit(proceedingJoinPoint,resourceLimit);
                default: ;
        }
        return null;
    }

    private Object proceedingConsumerRateLimit(ProceedingJoinPoint proceedingJoinPoint, ResourceLimit resourceLimit) throws Throwable {
        String s = resourceLimit.limitServiceBeanName();
        Object bean = ApplicationUtils.getBean(s);
        if(bean instanceof ResourceLimitService){
            return proceedingRateLimit(proceedingJoinPoint,resourceLimit,(ResourceLimitService)bean);
        }else{
            throw new ParseException(0,"类型转换异常");
        }
    }

    private Object proceedingRateLimit(ProceedingJoinPoint proceedingJoinPoint,ResourceLimit resourceLimit,ResourceLimitService resourceLimitService) throws Throwable {
        if(resourceLimitService.isExist(resourceLimit)){
            boolean b = resourceLimitService.proceedingRateLimit(resourceLimit);
            try{
                reentrantLock.readLock().lock();
                if(b){
                    return proceedingJoinPoint.proceed();
                }else{
                    return resourceLimitService.rateLimitFallback(resourceLimit,proceedingJoinPoint);
                }
            }finally {
                reentrantLock.readLock().unlock();
            }
        }
        return proceedingJoinPoint.proceed();
    }

}
