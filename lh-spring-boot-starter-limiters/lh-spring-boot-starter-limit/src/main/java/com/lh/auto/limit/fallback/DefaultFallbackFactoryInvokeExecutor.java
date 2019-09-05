package com.lh.auto.limit.fallback;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;

public class DefaultFallbackFactoryInvokeExecutor implements FallbackFactoryInvokeExcutor {

    @Override
    public Object invoke(Class clazz, String methodName, ProceedingJoinPoint joinPoint) throws Exception {
        Method method = getMethod(clazz,methodName,joinPoint);
        return invoke(joinPoint, method, getParameterTypes(method));
    }

    private Object invoke(ProceedingJoinPoint joinPoint, Method method, Parameter[] parameterTypes) throws Exception {
        try {
            List<Object> list = getParameters(parameterTypes, joinPoint);
            return method.invoke(null,list.toArray());
        } catch (Exception e) {
            throw new ReflectiveOperationException("fallback exception");
        }
    }

    private List<Object> getParameters(Parameter[] parameterTypes, ProceedingJoinPoint joinPoint) throws Exception {
        List<Object> list = new LinkedList<>();
        Object[] args = joinPoint.getArgs();
        int count = 0;
        for(Parameter parameter : parameterTypes){
            if(parameter.getType() == FallbackPojoInfo.class){
                list.add(parsePojoInfo(joinPoint));
            }else{
                list.add(args[count++]);
            }
        }
        return list;
    }


    private FallbackPojoInfo parsePojoInfo(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        FallbackPojoInfo.FallbackPojoInfoBuilder builder = FallbackPojoInfo.builder();
        Method currentMethod = getTargetMethod(joinPoint);
        return builder.method(currentMethod).parameters(joinPoint.getArgs()).target(joinPoint.getTarget()).build();
    }

    private Method getTargetMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Signature sig = joinPoint.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = joinPoint.getTarget();
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        return currentMethod;
    }


    private Parameter[] getParameterTypes(Method method) {
        return method.getParameters();
    }

    private Method getMethod(Class clazz, String methodName, ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        if(clazz == DefaultFallbackFactory.class && StringUtils.isEmpty(methodName)){
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getName().equals("fallback")) {
                    return method;
                }
            }
        }else if(clazz == DefaultFallbackFactory.class && !StringUtils.isEmpty(methodName)){
            Object target = joinPoint.getTarget();
            Method[] methods = target.getClass().getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    return method;
                }
            }
        }else if(clazz != DefaultFallbackFactory.class && StringUtils.isEmpty(methodName)){
            Method targetMethod = getTargetMethod(joinPoint);
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(targetMethod.getName())) {
                    return method;
                }
            }
        }else if(clazz != DefaultFallbackFactory.class && !StringUtils.isEmpty(methodName)){
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    return method;
                }
            }
        }

        return null;
    }


}
