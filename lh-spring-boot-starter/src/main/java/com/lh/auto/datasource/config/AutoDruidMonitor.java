package com.lh.auto.datasource.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

import com.lh.auto.datasource.conditional.DruidConditional;

/**
 * 
 * @author LH
 *	默认开启监控中心，如要关闭，请将spring.auto.druid.start-auto设置为false
 */
@ConditionalOnProperty(name="spring.auto.druid.start-auto",havingValue="true",matchIfMissing=true)
@Conditional(DruidConditional.class)
@EnableConfigurationProperties(DruidMonitorConfigProperties.class)
public class AutoDruidMonitor{
	private static Logger log = LoggerFactory.getLogger(AutoDruidMonitor.class);
	@Autowired
	private DruidMonitorConfigProperties prop;
	@Bean
	@ConditionalOnBean(DataSource.class)
	@Conditional(DruidConditional.class)
	public ServletRegistrationBean<Servlet> getReistrationBean() throws InstantiationException, IllegalAccessException, ClassNotFoundException{	
		ServletRegistrationBean<Servlet> servletRegistrationBean = new ServletRegistrationBean<>((Servlet)Class.forName("com.alibaba.druid.support.http.StatViewServlet").newInstance(),prop.getDruidPatten());
		Map<String,String> map = new HashMap<>();
		if(prop.isLogin()) {
			map.put("loginUsername", prop.getLoginUsername().trim());
			map.put("loginPassword", prop.getLoginPassword().trim());
		}
		map.put("allow", prop.getAllow());
		map.put("deny", prop.getDeny());
		servletRegistrationBean.setInitParameters(map);
		log.info("StatViewServlet初始化完毕");
		return servletRegistrationBean;	
	}
	@Bean
	@ConditionalOnBean(DataSource.class)
	@Conditional(DruidConditional.class)
	public FilterRegistrationBean<Filter> getFilterRegistrationBean() throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter((Filter)Class.forName("com.alibaba.druid.support.http.WebStatFilter").newInstance());
		filterRegistrationBean.addUrlPatterns(prop.getFilterPatten()); 
	       //添加不需要忽略的格式信息
	    filterRegistrationBean.addInitParameter("exclusions", prop.getExclusions());
	    log.info("WebStatFilter初始化完毕");
	    return filterRegistrationBean ;
	}
}
