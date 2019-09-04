package com.lh.auto.limit.fallback;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;

@Data
@Builder
public class FallbackPojoInfo {

    private Method method;

    private Object target;

    private Object[] parameters;

}
