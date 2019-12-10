package com.lhstack.annotation;

import com.lhstack.autoconfig.PluginConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Import(PluginConfiguration.class)
public @interface EnableAutoPlugin {
}
