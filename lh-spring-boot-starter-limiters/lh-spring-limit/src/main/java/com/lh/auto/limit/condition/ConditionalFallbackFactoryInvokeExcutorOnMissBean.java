package com.lh.auto.limit.condition;

import com.lh.auto.limit.fallback.FallbackFactoryInvokeExcutor;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ConditionalFallbackFactoryInvokeExcutorOnMissBean implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        FallbackFactoryInvokeExcutor bean = context.getBeanFactory().getBean(FallbackFactoryInvokeExcutor.class);
        return bean != null;
    }
}
