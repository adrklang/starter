### lh-spring-boot-starter-limit 介绍

```markdown
开启验证码功能
@EnableRateLimit

使用
	在指定方法上面使用@ResourceLimit注解
		
```

### 	ResourceLimit参数详解
```java
int capacity() default 15;
//令牌桶最大容量，只适用于JDKRateLimitService和自定义ResourceLimitServiece
```

```java
int initCapacity() default 10; 
//初始化容量，必须比capacity小，不然使用capacity的值
//只适用于JDKRateLimitService和自定义ResourceLimitServiec
```

```java
String key();//限流key
/**
LimitType = SESSION //key = session.id + key
LimitType = IP //key = ip + key
LimitType = NONE //key = key 如果使用此参数，那么限流针对所有人的请求
**/
```

```java
int seconds() default 1;
//LimitService = JKD 此参数代表在指定seconds内生成secondsAddCount枚令牌
//LimitService = RATE 此参数代表预热
```

```java
LimitType type() default LimitType.SESSION;
//NONE 对所有用户生效,SESSION基于SESSIONID限流,IP基于IP
```

```java
LimitService useLimitService() default LimitService.RATE;
//默认使用GuavaRateLimitService,
//LimitService.JDK =》 JDKRateLimitService
//LimitService.CONSUMER =》 用户自定义ResourceLimitService 此参数与limitServiceBeanName配合使用
```

```java
String limitServiceBeanName() default "";
//如果LimitService设置为CONSUMER,那么此属性生效,此属性为容器里面实现了ResourceLimitService接口的beanname,用户如果要自定义限流规则，使用此属性即可
```

```java
int secondsAddCount() default 1;
//每秒添加多少个令牌，此属性必须必capacity小，只适用于JDKRateLimitService与自定义ResourceLimitService
```

