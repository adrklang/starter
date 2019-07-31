### lh-spring-boot-starter-validcode 介绍

```markdown
开启验证码功能
@EnableValidCode

使用
	所在controller内注入ValidCodeService即可
需自定义配置验证码生成属性，只需修改application.yml文件即可
```

### lh-spring-validcode 介绍

```markdown
开启验证码功能
@EnableValidCode
Application-context.xml扫描到以上注解所在的类，即可开启

```

通过以下配置覆盖验证码生成属性即可

```xml
<!-- id="ValidCodeProperties"这个属性是必须的，否则无法实现覆盖配置 -->
<bean id="ValidCodeProperties" class="com.lh.auto.validcode.config.ValidCodeProperties">
        <property name="width" value="400"></property>
</bean>
```

