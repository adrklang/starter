package com.lh.auto.datasource.config.listener;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import com.lh.auto.datasource.annotation.EnableAutoConfigDataSource;
/**
 * 
 * @author LH
 *	获取主要启动类上面的注解
 */
public class AutoConfigDataSourceRunListener implements SpringApplicationRunListener {
	private static EnableAutoConfigDataSource anno;
	public AutoConfigDataSourceRunListener(SpringApplication application,String ...args) throws Exception {
		Set<Object> allSources = application.getAllSources();
		for (Object obj : allSources) {
			if(obj instanceof Class) {
				Class<?> clazz = (Class<?>) obj;
				Annotation[] annotations = clazz.getAnnotations();
				for (Annotation annot : annotations) {
					if(annot instanceof EnableAutoConfigDataSource) {
						anno = (EnableAutoConfigDataSource) annot;
						break;
					}
				}
				
				break;
			}
		}
		
	}
	public static final EnableAutoConfigDataSource getEnableAutoConfigDataSource() {
		return anno;
	}
	@Override
	public void starting() {
		// TODO Auto-generated method stub
	}

	@Override
	public void environmentPrepared(ConfigurableEnvironment environment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextPrepared(ConfigurableApplicationContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextLoaded(ConfigurableApplicationContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void started(ConfigurableApplicationContext context) {
		// TODO Auto-generated method stub
	}

	@Override
	public void running(ConfigurableApplicationContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void failed(ConfigurableApplicationContext context, Throwable exception) {
		// TODO Auto-generated method stub
		
	}

	

}
