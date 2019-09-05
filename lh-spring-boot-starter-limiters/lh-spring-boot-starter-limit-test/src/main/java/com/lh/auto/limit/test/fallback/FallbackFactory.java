package com.lh.auto.limit.test.fallback;


import com.lh.auto.limit.fallback.FallbackPojoInfo;

import javax.servlet.http.HttpServletRequest;

public class FallbackFactory extends TestService {

    /**
     * 除了FallbackPojoInfo参数顺序可以任意之外，其他参数必须按照要限流的方法中参数顺序一致
     * @param info
     * @param request
     * @return
     */
    public static String message(FallbackPojoInfo info,String message,  HttpServletRequest request){
        System.out.println(info);
        System.out.println("message method");
        System.out.println(message);
        return "hello " + info + request;
    }
    public static String message1(FallbackPojoInfo info,String message,  HttpServletRequest request){
        System.out.println(info);
        System.out.println("message 1 method");
        System.out.println(message);
        return "hello " + info + request;
    }
}
