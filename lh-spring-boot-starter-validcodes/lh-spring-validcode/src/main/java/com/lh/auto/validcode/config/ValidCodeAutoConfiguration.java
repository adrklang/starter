package com.lh.auto.validcode.config;

import com.lh.auto.validcode.service.ValidCodeService;
import com.lh.auto.validcode.service.impl.ValidCodeKaptcha;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

public class ValidCodeAutoConfiguration {
    @Bean("ValidCodeProperties")
    public ValidCodeProperties validCodeProperties(){
        return new ValidCodeProperties();
    }
    @Bean("myblogKaptcha")
    public ValidCodeService kaptcha(@Qualifier("ValidCodeProperties") ValidCodeProperties validCodeProperties){
        return new ValidCodeKaptcha(validCodeProperties);
    }
}
