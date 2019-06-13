package com.lh.auto.datasource.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="spring.auto.datasource.monitor")
public class DruidMonitorConfigProperties {
	/**
	 * 	监控中心拦截地址
	 */
	private String druidPatten = "/druid/*";
	/**
	 * 	druid监控中心用户名，不填此属性，默认不进行登陆验证
	 */
	private String loginUsername;
	/**
	 * 	druid监控中心密码
	 */
	private String loginPassword;
	
	/**
	 * 	默认允许所有能访问监控中心，填localhost就允许本地能访问
	 */
	private String allow="";
	
	/**
	 * 	拒绝谁访问，填写的是ip地址，多个ip用，隔开
	 */
	private String deny="";
	/**
	 * 	过滤器拦截地址
	 */
	private String filterPatten = "/*";
	/**
	 * 	配置那些地址不拦截
	 */
	private String exclusions = "*.js,*.gif,*.jpg,*.css,*.png,*.scss,*.bmp,*.mp4,/druid/*";
	public boolean isLogin() {
		if(this.loginUsername == null || this.loginUsername.trim().equals("")) {
			return false;
		}else {
			if( this.loginPassword == null || this.loginPassword.trim().equals("")) {
				return false;
			}else {
				return true;
			}
		}
	}

	public String getDruidPatten() {
		return druidPatten;
	}

	public void setDruidPatten(String druidPatten) {
		this.druidPatten = druidPatten;
	}

	public String getExclusions() {
		return exclusions;
	}

	public void setExclusions(String exclusions) {
		this.exclusions = exclusions;
	}

	public String getFilterPatten() {
		return filterPatten;
	}

	public void setFilterPatten(String filterPatten) {
		this.filterPatten = filterPatten;
	}

	public String getLoginUsername() {
		return loginUsername;
	}

	public void setLoginUsername(String loginUsername) {
		this.loginUsername = loginUsername;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getAllow() {
		return allow;
	}

	public void setAllow(String allow) {
		this.allow = allow;
	}

	public String getDeny() {
		return deny;
	}

	public void setDeny(String deny) {
		this.deny = deny;
	}
	
	@Override
	public String toString() {
		return "DruidMonitorConfigProperties [druidPatten=" + druidPatten + ", loginUsername=" + loginUsername
				+ ", loginPassword=" + loginPassword + ", allow=" + allow + ", deny=" + deny + ", filterPatten="
				+ filterPatten + "]";
	}

	
	
}
