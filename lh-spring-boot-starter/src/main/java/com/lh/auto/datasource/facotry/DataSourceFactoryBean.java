package com.lh.auto.datasource.facotry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lh.auto.datasource.config.DataSourceConfigProperties;
import com.lh.auto.datasource.enums.DbType;


/**
 * 
 * @author LH
 * 	连接池工厂
 *
 */
public class DataSourceFactoryBean {
	private static Logger log = LoggerFactory.getLogger(DataSourceFactoryBean.class);
	
	private DataSourceMethodParamBindUtils dataSourceMethodParamBindUtils = new DataSourceMethodParamBindUtils();
	
	public DataSource getDataSourceBean(DbType dbType, DataSourceConfigProperties prop) {
		// TODO Auto-generated method stub
		switch(dbType) {
			case HIKARICP:return getHikaricp(prop);
			case DRUID:return getDruid(prop);
			case DBCP:return getDbcp(prop);
			case DBCP2:return getDbcp2(prop);
			case C3P0:return getC3P0(prop);
		default:
			break;
		}
		return null;
	}
	
	private DataSource getC3P0(DataSourceConfigProperties prop) {
		// TODO Auto-generated method stub
		Class<?> clazz;
		try {
			clazz = Class.forName("com.mchange.v2.c3p0.ComboPooledDataSource");
			Object c3p0 = clazz.newInstance();
			dataSourceMethodParamBindUtils.methodBind(clazz,c3p0,prop);
			log.info("c3p0初始化完成");
			return (DataSource) c3p0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private DataSource getDbcp2(DataSourceConfigProperties prop) {
		// TODO Auto-generated method stub
		Class<?> clazz;
		try {
			clazz = Class.forName("org.apache.commons.dbcp2.BasicDataSource");
			Object dbcp2 = clazz.newInstance();
			dataSourceMethodParamBindUtils.methodBind(clazz,dbcp2,prop);
			log.info("Dbcp2初始化完成");
			return (DataSource) dbcp2;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private DataSource getDbcp(DataSourceConfigProperties prop) {
		// TODO Auto-generated method stub
		try {
			Class<?> clazz = Class.forName("org.apache.commons.dbcp.BasicDataSource");
			Object dbcp = clazz.newInstance();
			dataSourceMethodParamBindUtils.methodBind(clazz,dbcp,prop);
			log.info("Dbcp初始化完成");
			return (DataSource) dbcp;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private DataSource getDruid(DataSourceConfigProperties prop) {
		try {
			Class<?> clazz = Class.forName("com.alibaba.druid.pool.DruidDataSource");
			Object druid = clazz.newInstance();
			dataSourceMethodParamBindUtils.methodBind(clazz,druid,prop);
			log.info("Druid初始化完成");
			return (DataSource) druid;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	private DataSource getHikaricp(DataSourceConfigProperties prop) {
		// TODO Auto-generated method stub
		try {
			Class<?> clazz = Class.forName("com.zaxxer.hikari.HikariDataSource");
			Constructor<?> constructor = clazz.getConstructor(Class.forName("com.zaxxer.hikari.HikariConfig"));
			log.info("hikaricp初始化完成");
			return (DataSource) constructor.newInstance(getHikaricpConfig(prop));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private Object getHikaricpConfig(DataSourceConfigProperties prop) throws Exception {
		// TODO Auto-generated method stub
		Class<?> clazz = Class.forName("com.zaxxer.hikari.HikariConfig");
		Object hikaricp = clazz.newInstance();
		dataSourceMethodParamBindUtils.methodBind(clazz,hikaricp,prop);
		return hikaricp;
	}

	
	
	
	/**
	 * 	内部工具类
	 * @author LH
	 *
	 */
	private class DataSourceMethodParamBindUtils{
		public void methodBind(Class<?> clazz,Object target,DataSourceConfigProperties prop) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				dataSourceParamBind(target,method,prop);
			}
		}

		private void dataSourceParamBind(Object datasource, Method method, DataSourceConfigProperties prop) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			// TODO Auto-generated method stub
			if(method.getName().equals("setUsername")
					|| method.getName().equals("setUser")) {
				method.invoke(datasource, prop.getUsername());
			}else if(method.getName().equals("setPassword")) {
				method.invoke(datasource, prop.getPassword());
			}else if(method.getName().equals("setUrl") 
					|| method.getName().equals("setJdbcUrl")) {
				method.invoke(datasource, prop.getJdbcUrl());
			}else if(method.getName().equals("setDriverClassName")
					|| method.getName().equals("setDriverClass")) {
				method.invoke(datasource, prop.getDriverClassName());
			}else if(method.getName().equals("setInitialSize")
					|| method.getName().equals("setInitialPoolSize")) {
				method.invoke(datasource, prop.getInitialSize());
			}else if(method.getName().equals("setMaxActive")
					|| method.getName().equals("setMaximumPoolSize")
					|| method.getName().equals("setMaxPoolSize")
					|| method.getName().equals("setMaxIdle")) {
				method.invoke(datasource, prop.getMaxActive());
			}else if(method.getName().equals("setMaxWait")
					|| method.getName().equals("setMaxWaitMillis")) {
				method.invoke(datasource, prop.getMaxWait());
			}else if(method.getName().equals("setMaxOpenPreparedStatements")) {
				method.invoke(datasource, prop.getMaxOpenPreparedStatements());
			}else if(method.getName().equals("setMinIdle")
					|| method.getName().equals("setMinimumIdle")
					|| method.getName().equals("setMinPoolSize")) {
				method.invoke(datasource, prop.getMinIdle());
			}else if(method.getName().equals("setFilters")) {
				method.invoke(datasource, prop.getFilters());
			}else if(method.getName().equals("setPoolPreparedStatements")) {
				method.invoke(datasource, prop.isPoolPreparedStatements());
			}else if(method.getName().equals("setValidationQuery")
					|| method.getName().equals("setConnectionTestQuery")) {
				method.invoke(datasource, prop.getValidationQuery());
			}else if(method.getName().equals("setTestOnBorrow")) {
				method.invoke(datasource, prop.isTestOnBorrow());
			}else if(method.getName().equals("setTestWhileIdle")) {
				method.invoke(datasource, prop.isTestWhileIdle());
			}else if(method.getName().equals("setMinEvictableIdleTimeMillis")) {
				method.invoke(datasource, prop.getMinEvictableIdleTimeMillis());
			}else if(method.getName().equals("setTimeBetweenEvictionRunsMillis")) {
				method.invoke(datasource, prop.isTimeBetweenEvictionRunsMillis());
			}else if(method.getName().equals("setRemoveAbandoned")
					|| method.getName().equals("setRemoveAbandonedOnBorrow")) {
				method.invoke(datasource, prop.isRemoveAbandoned());
			}else if(method.getName().equals("setRemoveAbandonedTimeout")) {
				method.invoke(datasource, prop.getRemoveAbandonedTimeout());
			}else if(method.getName().equals("setAutoCommit")
					|| method.getName().equals("setAutoCommitOnClose")
					|| method.getName().equals("setDefaultAutoCommit")) {
				method.invoke(datasource, prop.isAutoCommit());
			}else if(method.getName().equals("setMaxIdleTime")) {
				method.invoke(datasource, (int)(prop.getMaxWait() / 1000));
			}else if(method.getName().equals("setMaxEvictableIdleTimeMillis")) {
				method.invoke(datasource, prop.getMaxEvictableIdleTimeMillis());
			}else if(method.getName().equals("setMaxPoolPreparedStatementPerConnectionSize")) {
				method.invoke(datasource, prop.getMaxPoolPreparedStatementPerConnectionSize());
			}else if(method.getName().equals("setTestOnReturn")) {
				method.invoke(datasource, prop.isTestOnReturn());
			}
		}
	}
}
