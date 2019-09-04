package com.lh.auto.limit.annotation;

import com.lh.auto.limit.model.LimitService;
import com.lh.auto.limit.model.LimitType;
import com.lh.auto.limit.fallback.DefaultFallbackFactory;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResourceLimit {
    int capacity() default 15;//最大容量，只适用于JDKRateLimitService
    int initCapacity() default 10; //初始化容量，必须比capacity小，不然使用capacity的值
    String key();//限流key
    int seconds() default 1;//JDKRateLimitService 下 seconds秒补充secondsAddCount个令牌,GuavaRateLimitService下 sencods秒补充1个令牌
    Class<?> fallbackFactory() default DefaultFallbackFactory.class;
    String method() default "fallback";
    LimitType type() default LimitType.SESSION;//NONE 对所有用户生效,SESSION基于SESSIONID限流,IP基于IP
    LimitService useLimitService() default LimitService.RATE;//默认使用GuavaRateLimitService,
    String limitServiceBeanName() default "";//如果LimitService设置为CONSUMER,那么此属性生效,此属性为容器里面实现了ResourceLimitService接口的beanname,用户如果要自定义限流规则，使用此属性即可
    int secondsAddCount() default 1;//每秒添加多少个令牌，此属性必须必capacity小，只适用于JDKRateLimitService

}
