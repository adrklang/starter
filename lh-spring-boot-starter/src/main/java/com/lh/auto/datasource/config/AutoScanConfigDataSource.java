package com.lh.auto.datasource.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.lh.auto.datasource.banner.BannerUtils;
import com.lh.auto.datasource.config.listener.AutoConfigDataSourceRunListener;
import com.lh.auto.datasource.facotry.DataSourceFactoryBean;

/**
 * 
 * @author LH
 *
 */
@EnableConfigurationProperties(DataSourceConfigProperties.class)
public class AutoScanConfigDataSource{
	@Autowired
	private DataSourceConfigProperties prop;
	@Bean("dataSource")
	@ConditionalOnClass(DataSource.class)
	@ConditionalOnMissingBean
	public DataSource getDataSource() throws Exception {
		if (prop.isBanner()) {
			BannerUtils.logBanner();
		}
		return (DataSource) new DataSourceFactoryBean().getDataSourceBean(AutoConfigDataSourceRunListener.getEnableAutoConfigDataSource().value(), prop);
	}
}
