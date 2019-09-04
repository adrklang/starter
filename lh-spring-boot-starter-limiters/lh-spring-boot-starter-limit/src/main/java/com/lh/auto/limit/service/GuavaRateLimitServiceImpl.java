package com.lh.auto.limit.service;

import com.lh.auto.limit.annotation.ResourceLimit;
import com.lh.auto.limit.config.ResourceLimitAutoConfiguration;
import com.lh.auto.limit.fallback.FallbackFactoryInvokeExcutor;
import com.lh.auto.limit.model.LimitType;
import com.lh.auto.limit.model.RateLimiter;
import com.lh.auto.limit.utils.IpUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class GuavaRateLimitServiceImpl implements ResourceLimitService{
    @Autowired(required = false)
    private HttpSession session;
    private static Logger logger = LoggerFactory.getLogger(GuavaRateLimitServiceImpl.class);
    private static ConcurrentHashMap<String, RateLimiter> concurrentHashMap = new ConcurrentHashMap<>();
    private static ExecutorService executorService = null;
    private static AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    @Autowired
    private FallbackFactoryInvokeExcutor fallbackFactoryInvokeExcutor;

    /**
     * guava限流缓存销毁时间
     */
    @Value("${resource.limit.guava.destroyTime:1800000}")
    private Long waitTimeDestroy;
    public void init(){
        if(executorService == null){
            executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() ->{
                while(true){
                    try {
                        //每过1s检查一次
                        Thread.sleep(1000);
                        Set<Map.Entry<String, RateLimiter>> entries = concurrentHashMap.entrySet();
                        for(Map.Entry<String, RateLimiter> s:entries){
                            String key = s.getKey();
                            RateLimiter value = s.getValue();
                            Long time = value.getTime();
                            Long currentTime = System.currentTimeMillis();
                            //30分钟
                            if(currentTime - time >= waitTimeDestroy){
                                concurrentHashMap.remove(key);
                                logger.info("Rate --- 限流对象超时，已删除:" + key);
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            logger.info("GuavaLimit Thread Listener Create Success,createTime --- {}",System.currentTimeMillis());
        }
    }

    @Override
    public Object rateLimitFallback(ResourceLimit resourceLimit, ProceedingJoinPoint joinPoint) throws Exception {
        return fallbackFactoryInvokeExcutor.invoke(resourceLimit.fallbackFactory(),resourceLimit.method(),joinPoint);
    }
    private void sessionLimit(ResourceLimit resourceLimit){
        String key = getSessionKey(resourceLimit);
        long initCapacity = resourceLimit.initCapacity();
        long seconds = resourceLimit.seconds();
        if(!concurrentHashMap.containsKey(key)){
            synchronized (ResourceLimitAutoConfiguration.class){
                if(!concurrentHashMap.containsKey(key)){
                    RateLimiter rateLimiter = RateLimiter.create(initCapacity, seconds, TimeUnit.SECONDS);
                    concurrentHashMap.put(key,rateLimiter);
                    logger.info("初始化RateLimiter -- {},type --- sessionLimit, key -- {}",rateLimiter,key);
                }
            }
        }
    }
    private boolean proceedingSessionRateLimit(ResourceLimit resourceLimit){
        String key = getSessionKey(resourceLimit);
        RateLimiter rateLimiter = concurrentHashMap.get(key);
        if(rateLimiter.tryAcquire()){
            return true;
        }
        return false;
    }
    private String getSessionKey(ResourceLimit resourceLimit){
        String key = session != null ? session.getId() + ":" + resourceLimit.key() : "SessionNone:" + resourceLimit.key();
        return key;
    }
    @Override
    public boolean proceedingRateLimit(ResourceLimit resourceLimit) {
        switch (resourceLimit.type()){
            case SESSION: return proceedingSessionRateLimit(resourceLimit);
            case IP:return proceedingIpRateLimit(resourceLimit);
            case NONE:return proceedingNoneRateLimit(resourceLimit);
            default:;
        }
        return false;
    }

    private boolean proceedingNoneRateLimit(ResourceLimit resourceLimit) {
        String key = resourceLimit.key();
        RateLimiter rateLimiter = concurrentHashMap.get(key);
        if(rateLimiter.tryAcquire()){
            return true;
        }
        return false;
    }

    @Override
    public Boolean isExist(ResourceLimit resourceLimit){
        if(!atomicBoolean.get()){
            synchronized (GuavaRateLimitServiceImpl.class){
                if(!atomicBoolean.get()){
                    atomicBoolean.set(true);
                    init();
                }
            }
        }
        LimitType type = resourceLimit.type();
        switch (type){
            case SESSION: sessionLimit(resourceLimit);break;
            case IP: ipLimit(resourceLimit);break;
            case NONE: noneLimit(resourceLimit);break;
            default:;
        }

        return true;
    }

    private void noneLimit(ResourceLimit resourceLimit) {
        long initCapacity = resourceLimit.initCapacity();
        long seconds = resourceLimit.seconds();
        String key = resourceLimit.key();
        if(!concurrentHashMap.containsKey(key)){
            synchronized (ResourceLimitAutoConfiguration.class){
                if(!concurrentHashMap.containsKey(key)){
                    RateLimiter rateLimiter = RateLimiter.create(initCapacity, seconds, TimeUnit.SECONDS);
                    concurrentHashMap.put(key,rateLimiter);
                    logger.info("初始化RateLimiter -- {},type --- ipLimit,key -- {}",rateLimiter,key);
                }
            }
        }
    }

    private HttpServletRequest getRequest(){
        try{
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return requestAttributes.getRequest();
        }catch (Exception ex){
            return null;
        }
    }

    private void ipLimit(ResourceLimit resourceLimit) {
        String ip = IpUtils.getIp(getRequest());
        String key = null;
        if(ip != null){
            if(ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")){
                key = "localhost:" + resourceLimit.key();
            }else{
                key = ip + ":" + resourceLimit.key();
            }
        }else{
            key = "IpNone" + ":" + resourceLimit.key();
        }
        long initCapacity = resourceLimit.initCapacity();
        long seconds = resourceLimit.seconds();
        if(!concurrentHashMap.containsKey(key)){
            synchronized (ResourceLimitAutoConfiguration.class){
                if(!concurrentHashMap.containsKey(key)){
                    RateLimiter rateLimiter = RateLimiter.create(initCapacity, seconds, TimeUnit.SECONDS);
                    concurrentHashMap.put(key,rateLimiter);
                    logger.info("初始化RateLimiter -- {},type --- ipLimit,key -- {}",rateLimiter,key);
                }
            }
        }
    }

    private boolean proceedingIpRateLimit(ResourceLimit resourceLimit) {
        String key = getIpKey(resourceLimit);
        RateLimiter rateLimiter = concurrentHashMap.get(key);
        if(rateLimiter.tryAcquire()){
            return true;
        }
        return false;
    }
    private String getIpKey(ResourceLimit resourceLimit){
        String ip = IpUtils.getIp(getRequest());
        String key = null;
        if(ip != null){
            if(ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")){
                key = "localhost:" + resourceLimit.key();
            }else{
                key = ip + ":" + resourceLimit.key();
            }
        }else{
            key = "IpNone" + ":" + resourceLimit.key();
        }
        return key;
    }
}
