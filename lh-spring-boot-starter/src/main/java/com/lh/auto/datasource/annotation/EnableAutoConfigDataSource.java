package com.lh.auto.datasource.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.lh.auto.datasource.config.AutoScanConfigDataSource;
import com.lh.auto.datasource.enums.DbType;

/**
 * 
 * @author LH
 *	默认配置druid连接池,只能用户main方法的启动类
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AutoScanConfigDataSource.class)
public @interface EnableAutoConfigDataSource {
	DbType value() default DbType.DRUID;
}
