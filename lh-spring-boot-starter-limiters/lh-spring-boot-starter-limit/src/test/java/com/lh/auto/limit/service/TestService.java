package com.lh.auto.limit.service;

import com.lh.auto.limit.annotation.ResourceLimit;
import com.lh.auto.limit.model.LimitService;
import com.lh.auto.limit.model.LimitType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class TestService {
    @ResourceLimit(key = "helloMap", capacity = 20,seconds = 5,fallbackFactory = FallBackFactory.class,method = "helloMap")
    public Map<String,String> helloMap(){
        Map<String,String> map = new HashMap<>();
        map.put("author","LH");
        map.put("age","21");
        return map;
    }
    @ResourceLimit(key="message",seconds = 1, capacity = 5,type = LimitType.IP,useLimitService = LimitService.JDK,secondsAddCount = 2)
    public List<String> helloList(){
        List<String> list = new LinkedList<>();
        list.add("LH");
        list.add("21");
        return list;
    }

    public static class FallBackFactory{
        public static Map<String,String> helloMap(){
            Map<String,String> map = new HashMap<>();
            map.put("author","LH");
            map.put("age","21");
            map.put("limit","true");
            return map;
        }
        public static List<String> helloList(){
            List<String> list = new LinkedList<>();
            list.add("LH");
            list.add("21");
            list.add("limit:true");
            return list;
        }
    }
}
