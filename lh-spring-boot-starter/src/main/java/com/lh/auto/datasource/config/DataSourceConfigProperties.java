package com.lh.auto.datasource.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 * @author LH
 *
 */
@ConfigurationProperties(prefix="spring.auto.datasource")
public class DataSourceConfigProperties {
	private String username="root";
	private String password="root";
	private String driverClassName="com.mysql.cj.jdbc.Driver";
	private String jdbcUrl;
	/**
	 * BANNER
	 */
	private boolean banner = true;
	
	/**
	 * 	提交方式，默认false
	 */
	private boolean autoCommit = false;
	/**
	 * 	初始化连接池大小
	 */
	private Integer initialSize=30;
	

	/**
	 * 	最大连接池大小
	 */
	private Integer maxActive=200;
	
	/**
	 *  最小连接池
	 */
	private Integer minIdle=20;
	
	/**
	 * 	最大等待时间
	 */
	private Long maxWait=600000L;
	
	
	/**
	 * 	要启用PSCache，必须配置大于0，当大于0时，
	 *	poolPreparedStatements自动触发修改为true。
	 *	在Druid中，不会存在Oracle下PSCache占用内存过多的问题，
	 *	可以把这个数值配置大一些，比如说100
	 */
	private Integer maxOpenPreparedStatements=1;
	
	/**
	 * 	检测sql是否有效
	 */
	private String validationQuery="SELECT 100";
	
	/**
	 * 	申请连接时执行validationQuery检测连接是否有效，
	 *	做了这个配置会降低性能。
	 */
	private boolean testOnBorrow=true;
	
	/**
	 * 	建议配置为true，不影响性能，并且保证安全性。
	 *	申请连接的时候检测，如果空闲时间大于
	 *	timeBetweenEvictionRunsMillis，
	 *	执行validationQuery检测连接是否有效。
	 */
	private boolean testWhileIdle=true;
	
	private boolean testOnReturn = false;
	/**
	 * 	Destory线程中如果检测到当前连接的最后活跃时间和当前时间的差值大于
	 *	minEvictableIdleTimeMillis，则关闭当前连接。
	 */
	private Long minEvictableIdleTimeMillis=30000L;
	/**
	 * 	连接池最大生存时间，必须大于minEvictableIdleTimeMillis
	 */
	private Long maxEvictableIdleTimeMillis=25200000L;
	/**
	 * 	检测链接时间间隔
	 */
	private Long timeBetweenEvictionRunsMillis=10000L;
	
	/**
	 * 	属性类型是字符串，通过别名的方式配置扩展插件，
	 *	常用的插件有：
	 *	监控统计用的filter:stat 
	 *	日志用的filter:log4j
	 *	 防御sql注入的filter:wall
	 */
	private String filters="stat,wall";
	/**
	 * 	是否缓存preparedStatement,PSCache
	 */
	private boolean poolPreparedStatements=true;
	/**
	 * 	打开PSCache，并且指定每个连接上PSCache的大小
	 */
	private int maxPoolPreparedStatementPerConnectionSize = 5;
	/**
	 * 	连接超时强制关闭连接
	 */
	private boolean removeAbandoned=true;
	
	/**
	 * 	连接建立多长时间被关闭,单位为m
	 */
	private Integer removeAbandonedTimeout=15;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public int getMaxPoolPreparedStatementPerConnectionSize() {
		return maxPoolPreparedStatementPerConnectionSize;
	}

	public void setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
		this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
	}

	public Integer getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(Integer initialSize) {
		this.initialSize = initialSize;
	}

	public Integer getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(Integer maxActive) {
		this.maxActive = maxActive;
	}

	public Integer getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	public Long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(Long maxWait) {
		this.maxWait = maxWait;
	}

	public boolean isPoolPreparedStatements() {
		return poolPreparedStatements;
	}

	public void setPoolPreparedStatements(boolean poolPreparedStatements) {
		this.poolPreparedStatements = poolPreparedStatements;
	}

	public Integer getMaxOpenPreparedStatements() {
		return maxOpenPreparedStatements;
	}

	public void setMaxOpenPreparedStatements(Integer maxOpenPreparedStatements) {
		this.maxOpenPreparedStatements = maxOpenPreparedStatements;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public Long getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(Long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public Long isTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(Long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public boolean isRemoveAbandoned() {
		return removeAbandoned;
	}

	public void setRemoveAbandoned(boolean removeAbandoned) {
		this.removeAbandoned = removeAbandoned;
	}

	public Integer getRemoveAbandonedTimeout() {
		return removeAbandonedTimeout;
	}

	public void setRemoveAbandonedTimeout(Integer removeAbandonedTimeout) {
		this.removeAbandonedTimeout = removeAbandonedTimeout;
	}
	public boolean isAutoCommit() {
		return autoCommit;
	}

	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	public Long getMaxEvictableIdleTimeMillis() {
		return maxEvictableIdleTimeMillis;
	}

	public void setMaxEvictableIdleTimeMillis(Long maxEvictableIdleTimeMillis) {
		this.maxEvictableIdleTimeMillis = maxEvictableIdleTimeMillis;
	}

	@Override
	public String toString() {
		return "DataSourceConfigProperties [username=" + username + ", password=" + password + ", driverClassName="
				+ driverClassName + ", jdbcUrl=" + jdbcUrl + ", banner=" + banner + ", autoCommit=" + autoCommit
				+ ", initialSize=" + initialSize + ", maxActive=" + maxActive + ", minIdle=" + minIdle + ", maxWait="
				+ maxWait + ", maxOpenPreparedStatements=" + maxOpenPreparedStatements + ", validationQuery="
				+ validationQuery + ", testOnBorrow=" + testOnBorrow + ", testWhileIdle=" + testWhileIdle
				+ ", testOnReturn=" + testOnReturn + ", minEvictableIdleTimeMillis=" + minEvictableIdleTimeMillis
				+ ", maxEvictableIdleTimeMillis=" + maxEvictableIdleTimeMillis + ", timeBetweenEvictionRunsMillis="
				+ timeBetweenEvictionRunsMillis + ", filters=" + filters + ", poolPreparedStatements="
				+ poolPreparedStatements + ", maxPoolPreparedStatementPerConnectionSize="
				+ maxPoolPreparedStatementPerConnectionSize + ", removeAbandoned=" + removeAbandoned
				+ ", removeAbandonedTimeout=" + removeAbandonedTimeout + "]";
	}

	public boolean isBanner() {
		// TODO Auto-generated method stub
		return this.banner;
	}

	public void setBanner(boolean banner) {
		this.banner = banner;
	}
	
}
