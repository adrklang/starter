package com.lh.auto.limit.utils;

import javax.servlet.http.HttpServletRequest;

public class IpUtils {
    public static String getIp(HttpServletRequest request){
        try{
            String ip = request.getHeader("x-real-getIp");
            if(ip == null){
                ip = request.getHeader("x-forwarded-for");
            }
            if(ip == null){
                ip = request.getHeader("x-forwarded-for");
            }
            if(ip == null){
                ip = request.getRemoteAddr();
            }
            return ip;
        }catch (Exception ex){
            return null;
        }
    }
}
