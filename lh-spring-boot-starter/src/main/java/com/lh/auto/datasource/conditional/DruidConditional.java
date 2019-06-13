package com.lh.auto.datasource.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.lh.auto.datasource.annotation.EnableAutoConfigDataSource;
import com.lh.auto.datasource.config.listener.AutoConfigDataSourceRunListener;
import com.lh.auto.datasource.enums.DbType;
/**
 * 
 * @author LH
 *	判断是否使用了DRUID连接池
 */
public class DruidConditional implements Condition {
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		// TODO Auto-generated method stub
		EnableAutoConfigDataSource enableAutoConfigDataSource = AutoConfigDataSourceRunListener.getEnableAutoConfigDataSource();
		if(enableAutoConfigDataSource != null) {
			DbType dbType = enableAutoConfigDataSource.value();
			if(dbType != null && dbType == DbType.DRUID) {
				return true;
			}
		}
		return false;
	}

}
