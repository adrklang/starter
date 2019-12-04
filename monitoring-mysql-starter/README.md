@[TOC]
## monitoring-mysql 
```markdown
   monitoring-mysql 是一款用于对mysql-connector-java底层执行sql时进行代理
   通过此代理可以获取sql语句，数据以及mysql执行时间进行监控,同时，它只用于对DataSource进行代理
   用户可以通过使用此starter对springboot应用里面的datasource进行自动代理
```
## 扩展
```markdown
如果要对monitoring-mysql进行扩展，可以通过以下方式实现
```
```java
package com.example.demo;

import com.lhstack.config.proxy.observer.AbstractObServerConsumer;
import com.lhstack.config.proxy.observer.ObServer;
import org.springframework.context.ApplicationContext;

public class MyObServer extends AbstractObServerConsumer {
    @Override
    public Object apply(ObServer obServer) throws Exception {
        ApplicationContext applicationContext = ApplicationHolder.applicationContext;
        System.out.println(applicationContext);
        return obServer.apply();
    }
}

```
```java
  package com.example.demo;
  
  import com.example.demo.entity.User;
  import com.example.demo.repository.UserRepository;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.boot.CommandLineRunner;
  import org.springframework.boot.SpringApplication;
  import org.springframework.boot.autoconfigure.SpringBootApplication;
  
  import java.util.List;
  
  @SpringBootApplication
  public class DemoApplication implements CommandLineRunner {
  
      @Autowired
      private UserRepository userRepository;
      public static void main(String[] args) {
          //覆盖默认配置，如果要使用Spring容器里面的bean，可以通过ApplicationContextAware获取ApplicationContext容器，使用静态方法方式获取bean即可
          System.setProperty("proxy.observer.class","com.example.demo.MyObServer");
          SpringApplication.run(DemoApplication.class, args);
      }
  
      @Override
      public void run(String... args) throws Exception {
          List<User> all = userRepository.findAll();
          System.out.println(all);
      }
  }

```

## 非springboot项目，引入monitoring-mysql这个包即可
```markdown
通过将DataSource通过ProxyDataSource类进行代理即可，其次和以上使用教程类似
```