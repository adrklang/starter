package com.lhstack.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Field;
import java.util.Map;

public class BeanUtils {
    public static String getKey(String defaultKey, Class<?> clazz) {
        if(StringUtils.isNotBlank(defaultKey)){
            return defaultKey;
        }
        String key = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
        return key.substring(0,1).toLowerCase() + key.substring(1,key.length());
    }

    public static void parameterBind(Map<String,Object> properties, Field field,Object target) throws Exception {
        Value value = field.getAnnotation(Value.class);
        field.setAccessible(true);
        if(value != null){
            String v = value.value();
            String[] values = parseKeyDefault(v);
            if(properties.containsKey(values[0])){
                beanParameterBind(field,target,(String) properties.get(values[0]));
            }else{
                beanParameterBind(field,target,values[0]);
            }
        }
    }

    private static void beanParameterBind(Field field, Object target, String value) throws Exception {
        Class type = field.getType();
        if(type == String.class || type == CharSequence.class){
            field.set(target,value);
        }else if(type == Integer.class || type == int.class){
            field.set(target,Integer.parseInt(value));
        }else if(type == Double.class || type == double.class){
            field.set(target,Double.parseDouble(value));
        }else if(type == Float.class || type == float.class){
            field.set(target,Float.parseFloat(value));
        }else if(type == Short.class || type == short.class){
            field.set(target,Short.parseShort(value));
        }else if(type == Long.class || type == long.class){
            field.set(target,Long.parseLong(value));
        }else if(type == Byte.class || type == byte.class){
            field.set(target,Byte.parseByte(value));
        }else if(type == Character.class || type == char.class){
            field.set(target,value.charAt(0));
        }else if(type == Boolean.class || type == boolean.class){
            field.set(target,Boolean.parseBoolean(value));
        }else{
            complexParameterBind(field, target, value);
        }
    }

    private static void complexParameterBind(Field field, Object target, String value) throws IllegalAccessException {
        String[] values = value.split(",");
        if(values.length == 1 || values.length == 0){
            values = value.split(" ");
        }
        Class<?> type = field.getType();
        if(type == Integer[].class || type == int[].class){
            Integer[] vs = new Integer[values.length];
            for (int i = 0; i < values.length; i++) {
                vs[i] = Integer.parseInt(values[i]);
            }
            field.set(target,vs);
        }else if(type == Double[].class || type == double[].class){
            Double[] vs = new Double[values.length];
            for (int i = 0; i < values.length; i++) {
                vs[i] = Double.parseDouble(values[i]);
            }
            field.set(target,vs);
        }else if(type == Float[].class || type == float[].class){
            Float[] vs = new Float[values.length];
            for (int i = 0; i < values.length; i++) {
                vs[i] = Float.parseFloat(values[i]);
            }
            field.set(target,vs);
        }else if(type == Short[].class || type == short[].class){
            Short[] vs = new Short[values.length];
            for (int i = 0; i < values.length; i++) {
                vs[i] = Short.parseShort(values[i]);
            }
            field.set(target,vs);
        }else if(type == Long[].class || type == long[].class){
            Long[] vs = new Long[values.length];
            for (int i = 0; i < values.length; i++) {
                vs[i] = Long.parseLong(values[i]);
            }
            field.set(target,vs);
        }else if(type == Character[].class || type == char[].class){
            char[] vs = new char[values.length];
            if(values.length == 1){
                String value1 = values[0];
                vs = value1.toCharArray();
            }else{
                for (int i = 0; i < values.length; i++) {
                    vs[i] = values[i].charAt(0);
                }
            }
            field.set(target,vs);
        }else if(type == Boolean[].class || type == boolean[].class){
            Boolean[] vs = new Boolean[values.length];
            for (int i = 0; i < values.length; i++) {
                vs[i] = Boolean.parseBoolean(values[i]);
            }
            field.set(target,vs);
        }else if(type == Byte[].class || type == byte[].class){
            Byte[] vs = new Byte[values.length];
            for (int i = 0; i < values.length; i++) {
                vs[i] = Byte.parseByte(values[i]);
            }
            field.set(target,vs);
        }else if(type == String[].class || type == CharSequence[].class){
            String[] vs = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                vs[i] = values[i];
            }
            field.set(target,vs);
        }
    }


    private static String[] parseKeyDefault(String v) {
        String value = v.substring(1 + 1, v.length() - 1);
        return value.split(":");
    }
}
