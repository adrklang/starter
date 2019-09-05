package com.lh.auto.limit.annotation;

import com.lh.auto.limit.model.LimitService;
import com.lh.auto.limit.model.LimitType;
import com.lh.auto.limit.fallback.DefaultFallbackFactory;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResourceLimit {
    /**
     * 限流最大容量，只适用于JDKRateLimitService
     * @return
     */
    int capacity() default 15;

    /**
     *  初始化容量，必须比capacity小，不然使用capacity的值
     * @return
     */
    int initCapacity() default 10;

    /**
     *  限流key
     * @return
     */
    String key();

    /**
     *  JDKRateLimitService 下 seconds秒补充secondsAddCount个令牌,GuavaRateLimitService下 sencods秒补充1个令牌
     * @return
     */
    int seconds() default 1;

    /**
     *   回调工厂类
     * @return
     */
    Class<?> fallbackFactory() default DefaultFallbackFactory.class;

    /**
     *
     *  注意: //TODO 限流回调方法除FallbackPojoInfo在方法形参中顺序可以任意以外，其他参数必须按照被限流方法的形参顺序一致 同时回调方法必须为静态方法
     *  例:  message(String message,HttpServletRequest req)  那么限流回调方法参数可以为  message //TODO 此方法名根据注解来决定 message(String message,HttpServletRequest req)
     *       message(FallbackPojoInfo info,String message,HttpServletRequest req), message(String message,FallbackPojoInfo info,HttpServletRequest req);
     * 例子:
     *  1.
     *     @GetMapping("hello/{message}")
     *     @ResourceLimit(key="message",seconds = 1, capacity = 5,type = LimitType.NONE,useLimitService = LimitService.JDK,secondsAddCount = 2)
     *     public String message(@PathVariable("message") String message, HttpServletRequest request){
     *         return "hello " + message;
     *     }
     *     那么使用默认限流方法
     *   2.
     *
     *      添加参数   method="fallback"
     *
     *
     *      @GetMapping("hello/{message}")
     *      @ResourceLimit(key="message",seconds = 1, //TODO method="fallback",  capacity = 5,type = LimitType.NONE,useLimitService = LimitService.JDK,secondsAddCount = 2)
     *      public String message(@PathVariable("message") String message, HttpServletRequest request){
     *             return "hello " + message;
     *      }
     *      那么使用当前被限流对象下面的 fallback方法
     *
     *   3.
     *
     *     @GetMapping("hello/{message}")
     *     @ResourceLimit(key="message",seconds = 1,//TODO fallbackFactory = FallbackFactory.class, capacity = 5,type = LimitType.NONE,useLimitService = LimitService.JDK,secondsAddCount = 2)
     *     public String message(@PathVariable("message") String message, HttpServletRequest request){
     *         return "hello " + message;
     *     }
     *
     *     那么会使用FallbackFactory.class里面与当前被限流对象同名的方法作为限流回调方法
     *
     *
     *   4.
     *
     *     @GetMapping("hello/{message}")
     *     @ResourceLimit(key="message",seconds = 1, // TODO fallbackFactory = FallbackFactory.class,method = "fallback" capacity = 5,type = LimitType.NONE,useLimitService = LimitService.JDK,secondsAddCount = 2)
     *     public String message(@PathVariable("message") String message, HttpServletRequest request){
     *         return "hello " + message;
     *     }
     *
     *     那么会使用FallbackFactory.class里面与method值相同的方法作为回调限流方法
     *
     * @return
     */
    String method() default "";

    /**
     * NONE 对所有用户请求限制,SESSION基于SESSIONID限流,IP基于IP
     * @return
     */
    LimitType type() default LimitType.SESSION;

    /**
     *  默认使用GuavaRateLimitService,有JDK ,CONSUMER
     *  JDK使用内置的基于内存方式限流
     *  CONSUMER 是告诉用户需要自定义限流对象
     *  与limitServiceBeanName参数配合使用
     * @return
     */
    LimitService useLimitService() default LimitService.RATE;

    /**
     * 如果LimitService设置为CONSUMER,那么此属性生效,此属性为容器里面实现了ResourceLimitService接口的beanname,用户如果要自定义限流规则，使用此属性即可
     * @return
     */
    String limitServiceBeanName() default "";

    /**
     *  每秒添加多少个令牌，此属性必须必capacity小，只适用于JDKRateLimitService
     * @return
     */
    int secondsAddCount() default 1;

}
