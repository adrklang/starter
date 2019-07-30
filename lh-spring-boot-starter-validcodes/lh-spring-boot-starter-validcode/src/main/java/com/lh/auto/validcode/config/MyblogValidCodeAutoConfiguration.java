package com.lh.auto.validcode.config;

import com.lh.auto.validcode.service.ValidCodeService;
import com.lh.auto.validcode.service.impl.MyblogKaptcha;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(MyblogValidCodeProperties.class)
public class MyblogValidCodeAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public MyblogValidCodeProperties myblogValidCodeProperties(){
        return new MyblogValidCodeProperties();
    }

    @Bean("myblogKaptcha")
    @ConditionalOnMissingBean
    @ConditionalOnBean(MyblogValidCodeProperties.class)
    public ValidCodeService myblogKaptcha(MyblogValidCodeProperties myblogValidCodeProperties){
        return new MyblogKaptcha(myblogValidCodeProperties);
    }

}
