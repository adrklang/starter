package com.lh.auto.validcode.annotation;

import com.lh.auto.validcode.config.ValidCodeAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Import({ValidCodeAutoConfiguration.class})
//用于开启验证码工具类，可以通过Autowrite自动注入MyblogKaptcha对象
public @interface EnableValidCode {
}
