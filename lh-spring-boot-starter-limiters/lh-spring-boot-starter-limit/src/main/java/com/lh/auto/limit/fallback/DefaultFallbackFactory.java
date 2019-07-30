package com.lh.auto.limit.fallback;

import com.lh.auto.limit.exception.LimitException;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
@Slf4j
public class DefaultFallbackFactory {
    public static void fallback(Object ... args){
        System.out.print("限流默认回调方法:");
        Arrays.asList(args).forEach(item ->{
            System.out.print(" 参数 --- " + item);
        });
        System.out.println();
        throw new LimitException("限流方法执行");
    }
}
