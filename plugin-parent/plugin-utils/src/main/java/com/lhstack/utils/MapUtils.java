package com.lhstack.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MapUtils {

    public static Map<String,String> propertiesToMap(Properties properties){
        return properties.entrySet().stream().collect(Collectors.toMap(item -> item.getKey().toString(),item -> item.getValue().toString()));
    }
    public static Map<String,String> aggregationMap(Map<String,String> ...maps){
        return Arrays.asList(maps).stream().flatMap(item -> item.entrySet().stream()).collect(MiniMap::new,(map, entry) ->{
            map.put(entry.getKey(),entry.getValue());
        }, (map,map2) ->{
            map2.entrySet().forEach(item ->{
                map.put(item.getKey(),item.getValue());
            });
        });
    }
    private static class MiniMap<K,V> extends ConcurrentHashMap<K,V>{
        @Override
        public V put(K key, V value) {
            if(super.containsKey(key)){
                super.remove(key);
            }
            return super.put(key, value);
        }
    }

    public static void main(String[] args) {
        Map<String,String> map1 = new HashMap<>();
        Map<String,String> map2 = new HashMap<>();
        map1.put("h","1");
        map2.put("h","1");
        map1.put("h1","1");
        map2.put("h2","1");
        Map<String, String> map = aggregationMap(map1, map2);
        System.out.println(map);
    }
}
