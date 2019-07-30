package com.lh.auto.limit.test.fallback;

public class FallbackFactory {
    public static String message(String message){
        System.out.println("ip:");
        return message + "/resourceLimit" + "ip:";
    }
}
