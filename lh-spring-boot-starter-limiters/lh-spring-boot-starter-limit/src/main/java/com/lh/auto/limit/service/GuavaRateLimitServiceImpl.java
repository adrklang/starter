package com.lh.auto.limit.service;

import com.lh.auto.limit.annotation.ResourceLimit;
import com.lh.auto.limit.config.ResourceLimitAutoConfiguration;
import com.lh.auto.limit.fallback.DefaultFallbackFactory;
import com.lh.auto.limit.model.LimitType;
import com.lh.auto.limit.model.RateLimiter;
import com.lh.auto.limit.utils.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GuavaRateLimitServiceImpl implements ResourceLimitService{
    @Autowired(required = false)
    private HttpSession session;
    private static Logger logger = LoggerFactory.getLogger(GuavaRateLimitServiceImpl.class);
    private static ReentrantReadWriteLock reentrantLock = new ReentrantReadWriteLock();
    private static ConcurrentHashMap<String,RateLimiter> concurrentHashMap = new ConcurrentHashMap<>();
    private static ReentrantLock writeLock = new ReentrantLock();
    @PostConstruct
    public void listener(){
        new Thread(() ->{
            while(true){
                try {
                    Thread.sleep(1000);//每过1s检查一次
                    Set<Map.Entry<String, RateLimiter>> entries = concurrentHashMap.entrySet();
                    for(Map.Entry<String, RateLimiter> s:entries){
                        String key = s.getKey();
                        RateLimiter value = s.getValue();
                        Long time = value.getTime();
                        Long currentTime = System.currentTimeMillis();
                        if(currentTime - time >= 1800000){//30分钟
                            try{
                                writeLock.lock();
                                concurrentHashMap.remove(key);
                                logger.info("Rate --- 限流对象超时，已删除:" + key);
                            }finally {
                                writeLock.unlock();
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    public Object rateLimitFallback(ResourceLimit resourceLimit,Object ... args) throws Exception {
        Class<?> fallbackFactoryClass = resourceLimit.fallbackFactory();
        if(fallbackFactoryClass == DefaultFallbackFactory.class){
            Method fallback = fallbackFactoryClass.getMethod("fallback",Object.class);
            Object invoke = fallback.invoke(null,resourceLimit.key());
            return invoke;
        }
        Method[] methods = fallbackFactoryClass.getMethods();
        Method targetMethod = null;
        for(Method method : methods){
            if(method.getName().equals(resourceLimit.method())){
                targetMethod = method;
                break;
            }
        }
        Object invoke = targetMethod.invoke(null, args);
        return invoke;
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
        try{
            reentrantLock.writeLock().lock();
            RateLimiter rateLimiter = concurrentHashMap.get(key);
            if(rateLimiter.tryAcquire()){
                return true;
            }
            return false;
        }finally {
            reentrantLock.writeLock().unlock();
        }
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
        try{
            reentrantLock.writeLock().lock();
            RateLimiter rateLimiter = concurrentHashMap.get(key);
            if(rateLimiter.tryAcquire()){
                return true;
            }
            return false;
        }finally {
            reentrantLock.writeLock().unlock();
        }
    }

    @Override
    public Boolean isExist(ResourceLimit resourceLimit){
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
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
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
        try{
            reentrantLock.writeLock().lock();
            RateLimiter rateLimiter = concurrentHashMap.get(key);
            if(rateLimiter.tryAcquire()){
                return true;
            }
            return false;
        }finally {
            reentrantLock.writeLock().unlock();
        }
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
