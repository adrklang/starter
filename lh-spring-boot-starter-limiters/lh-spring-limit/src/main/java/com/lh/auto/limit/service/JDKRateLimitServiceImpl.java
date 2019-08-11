package com.lh.auto.limit.service;

import com.lh.auto.limit.annotation.ResourceLimit;
import com.lh.auto.limit.fallback.DefaultFallbackFactory;
import com.lh.auto.limit.utils.IpUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class JDKRateLimitServiceImpl implements ResourceLimitService {
    private static ConcurrentHashMap<String, JDKLimit> cache = null;
    private static ExecutorService executorService = null;
    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    @Value("${resource.limit.jdk.destroyTime:1800000}")
    private Long waitTimeDestroy;
    static {
        cache = new ConcurrentHashMap<>();
    }
    public void init(){
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() ->{
            while (true) {
                try {
                    Thread.sleep(1000);
                    Set<Map.Entry<String, JDKLimit>> entries = cache.entrySet();
                    for (Map.Entry<String, JDKLimit> entry : entries) {
                        String key = entry.getKey();
                        JDKLimit value = entry.getValue();
                        Long limitCreateTime = value.getCreateTime();
                        Long currentTime = System.currentTimeMillis();
                        if (currentTime - limitCreateTime > waitTimeDestroy) {
                            cache.remove(key);
                            log.info("JDK限流 --- 限流对象超时，已删除:" + key);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        log.info("JDKLimit Thread Listener Create Success,createTime --- {}",System.currentTimeMillis());
    }
    @Override
    public Boolean isExist(ResourceLimit resourceLimit) {
        //第一次限流创建之后创建线程监听销毁
        if(!atomicBoolean.get()){
            synchronized (JDKRateLimitServiceImpl.class){
                if(!atomicBoolean.get()){
                    atomicBoolean.set(true);
                    init();
                }
            }
        }
        switch (resourceLimit.type()) {
            case SESSION:
                sessionLimitInit(resourceLimit);
                break;
            case IP:
                ipLimitInit(resourceLimit);
                break;
            case NONE:
                noneLimitInit(resourceLimit);
                break;
            default:
                ;
        }
        return true;
    }

    private void noneLimitInit(ResourceLimit resourceLimit) {
        commonCreateLimit(resourceLimit.key(), resourceLimit);
    }

    //获取ip限流规则的key
    private String getIpKey(ResourceLimit resourceLimit) {
        String ip = IpUtils.getIp(InnerUtils.getRequest());
        String key = null;
        if (ip != null) {
            if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) {
                key = "localhost:" + resourceLimit.key();
            } else {
                key = ip + ":" + resourceLimit.key();
            }
        } else {
            key = "IpNone" + ":" + resourceLimit.key();
        }
        return key;
    }

    private void ipLimitInit(ResourceLimit resourceLimit) {
        commonCreateLimit(getIpKey(resourceLimit), resourceLimit);
    }

    private String getSessionKey(ResourceLimit resourceLimit) {
        String id = InnerUtils.getSession().getId();
        String key = id != null ? id + ":" + resourceLimit.key() : "NoneSession:" + resourceLimit.key();
        return key;
    }

    //通用创建限流缓存
    private void commonCreateLimit(String key, ResourceLimit resourceLimit) {
        if (!cache.containsKey(key)) {
            synchronized (JDKRateLimitServiceImpl.class) {
                if (!cache.containsKey(key)) {
                    JDKLimit jdkLimit = JDKLimit.create(resourceLimit.seconds(), resourceLimit.initCapacity(), resourceLimit.capacity(), resourceLimit.secondsAddCount());
                    cache.put(key, jdkLimit);
                    log.info("初始化JDKLimiter -- {}, key -- {}", jdkLimit, key);
                }
            }
        }
    }

    private void sessionLimitInit(ResourceLimit resourceLimit) {
        commonCreateLimit(getSessionKey(resourceLimit), resourceLimit);
    }

    @Override
    public boolean proceedingRateLimit(ResourceLimit resourceLimit) {
        switch (resourceLimit.type()) {
            case NONE:
                return commonProceedingRateLimit(resourceLimit.key());
            case IP:
                return commonProceedingRateLimit(getIpKey(resourceLimit));
            case SESSION:
                return commonProceedingRateLimit(getSessionKey(resourceLimit));
            default:
                return false;
        }
    }

    private boolean commonProceedingRateLimit(String key) {
        JDKLimit jdkLimit = cache.get(key);
        return jdkLimit.tryAcquire();
    }

    @Override
    public Object rateLimitFallback(ResourceLimit resourceLimit, Object... args) throws Exception {
        Class<?> fallbackFactoryClass = resourceLimit.fallbackFactory();
        if (fallbackFactoryClass == DefaultFallbackFactory.class) {
            Method fallback = fallbackFactoryClass.getMethod("fallback", Object.class);
            Object invoke = fallback.invoke(null, resourceLimit.key());
            return invoke;
        }
        Method[] methods = fallbackFactoryClass.getMethods();
        Method targetMethod = null;
        for (Method method : methods) {
            if (method.getName().equals(resourceLimit.method())) {
                targetMethod = method;
                break;
            }
        }
        Object invoke = targetMethod.invoke(null, args);
        return invoke;
    }

    @Data
    @Accessors
    private static class JDKLimit {
        //令牌补充间隔时间
        private Integer seconds = 1;
        //修改时间
        private AtomicLong modificationTime = new AtomicLong();
        //创建时间
        private Long createTime;
        //每次补充令牌个数
        private Integer secondsAddCount;
        //以双端队列方式存储令牌
        private Deque<Integer> queue;
        //容量
        private Integer capacity;

        private JDKLimit(Integer seconds, Integer initCapacity, Integer capacity, Integer secondsAddCount) {
            if (capacity < initCapacity) {
                initCapacity = capacity;
            }
            if (secondsAddCount > capacity) {
                secondsAddCount = capacity;
            }
            this.seconds = seconds;
            this.capacity = capacity;
            this.secondsAddCount = secondsAddCount;
            queue = new LinkedBlockingDeque<>(capacity);
            modificationTime.set(System.currentTimeMillis());
            init(initCapacity);
            createTime = System.currentTimeMillis();
        }

        public static JDKLimit create(Integer seconds, Integer initCapacity, Integer capacity, Integer secondsAddCount) {
            return new JDKLimit(seconds, initCapacity, capacity, secondsAddCount);
        }

        public boolean tryAcquire() {
            try {
                boolean offer = this.offer();
            } finally {
                Integer poll = queue.poll();
                return poll != null;
            }
        }

        private boolean offer() {
            long timeMillis = System.currentTimeMillis();
            Long differenceTime = timeMillis - modificationTime.get();
            Long count = differenceTime / 1000 / seconds;
            if(log.isDebugEnabled() && count < 1){
                log.debug("There are {} tokens left in the container，Wait {} to get {} tokens",queue.size(),(seconds * 1000) - differenceTime,secondsAddCount);
            }
            boolean flag = false;
            count = count * secondsAddCount;
            long sum = count + queue.size();
            if (sum > capacity) {
                count = (long) capacity - queue.size();
            }
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    if (!flag) {
                        flag = queue.offer(i);
                    } else {
                        queue.offer(i);
                    }
                }
                modificationTime.set(System.currentTimeMillis());
            } else {
                flag = false;
            }
            return flag;
        }

        private void init(Integer initCapacity) {
            for (int i = 0; i < initCapacity; i++) {
                queue.offer(i);
            }
        }
    }

    //获取request和session
    private static class InnerUtils {
        public static HttpServletRequest getRequest() {
            try {
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                return requestAttributes.getRequest();
            } catch (Exception ex) {
                return null;
            }
        }

        public static HttpSession getSession() {
            try {
                HttpSession session = getRequest().getSession();
                return session;
            } catch (Exception ex) {
                return null;
            }
        }
    }
}
