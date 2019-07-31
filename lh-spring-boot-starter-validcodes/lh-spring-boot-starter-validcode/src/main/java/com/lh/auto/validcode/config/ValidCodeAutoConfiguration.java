package com.lh.auto.validcode.config;

import com.lh.auto.validcode.service.ValidCodeService;
import com.lh.auto.validcode.service.impl.ValidCodeKaptcha;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(ValidCodeProperties.class)
public class ValidCodeAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public ValidCodeProperties myblogValidCodeProperties(){
        return new ValidCodeProperties();
    }

    @Bean("myblogKaptcha")
    @ConditionalOnMissingBean
    @ConditionalOnBean(ValidCodeProperties.class)
    public ValidCodeService myblogKaptcha(ValidCodeProperties validCodeProperties){
        return new ValidCodeKaptcha(validCodeProperties);
    }

}
