package com.lhstack.core.parameter;

import com.lhstack.api.bind.ParameterBind;
import com.lhstack.core.utils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

public class DefaultParameterBindImpl implements ParameterBind {
    @Override
    public Object getObject(Map<String, Object> properties, Object bean) {
        String enable = (String) properties.get("com.lhstack.core.parameter.DefaultParameterBindImpl.enable");
        if(StringUtils.isBlank(enable) || Boolean.parseBoolean(enable)){
            init(properties,bean);
        }
        return bean;
    }

    private void init(Map<String, Object> properties, Object bean) {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Arrays.asList(fields).forEach(item ->{
            try {
                BeanUtils.parameterBind(properties,item,bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public long getOrder() {
        return 0;
    }
}
