#### 自动配置指定数据库连接池，同时配备最全属性，如果选用druid连接池，默认配置监控中心

````java

//数据库用户名 default:root type:String
spring.auto.datasource.username

//数据库密码 default:root type:String
spring.auto.datasource.password

//数据库驱动 default:com.mysql.cj.Driver type:String
spring.auto.datasource.driverClassName

//数据库url type:String
spring.auto.datasource.jdbcUrl

//banner default:true type:boolean
spring.auto.datasource.banner

//是否自动提交 default:false type:boolean
spring.auto.datasource.autoCommit

//初始化连接池大小 default:30 type:int
spring.auto.datasource.initialSize

//最大连接池 default:200 type:int
spring.auto.datasource.maxActive

//最小连接池 default：20 type:int
spring.auto.datasource.minIdle

//最大等待时间 default:600000 type:long 单位:毫秒
spring.auto.datasource.maxWait

//是否启用PsCache default:1 type:int 大于1表示启动 小于0表示不启动
spring.auto.datasource.maxOpenPreparedStatements

//检测sql是否有效 default:SELECT 100 type:String
spring.auto.datasource.validationQuery

//申请连接时执行validationQuery检测连接是否有效， default:true type:boolean
spring.auto.datasource.testOnBorrow

//建议配置为true，不影响性能，并且保证安全性。default:true type:boolean
spring.auto.datasource.testWhileIdle

//default:false type:boolean
spring.auto.datasource.testOnReturn

//连接池最小生存时间 default:30000 type:long 单位:毫秒
spring.auto.datasource.minEvictableIdleTimeMillis

//连接池最大生存时间，必须大于minEvictableIdleTimeMillis default:25200000 type:long 单位:毫秒
spring.auto.datasource.maxEvictableIdleTimeMillis

//检测链接时间间隔 default:10000 type:long 单位:毫秒
spring.auto.datasource.timeBetweenEvictionRunsMillis

//druid监控中心过滤器 default:stat,wall type:String
spring.auto.datasource.filters

//是否缓存preparedStatement,PSCache default:true type:boolean
spring.auto.datasource.poolPreparedStatements

//打开PSCache，并且指定每个连接上PSCache的大小 default:5 type:int
spring.auto.datasource.maxPoolPreparedStatementPerConnectionSize

//连接超时强制关闭连接 default:true type:boolean
spring.auto.datasource.removeAbandoned

//连接建立多长时间被关闭,单位为m default:15 type:int
spring.auto.datasource.removeAbandonedTimeout
````



````java


//druid监控中心相关配置

//监控中心地址 default:/druid/* type:String
spring.auto.datasource.monitor.druidPatten

//监控中心登录用户 default:不验证 type:String
spring.auto.datasource.monitor.loginUsername

//监控中心登录密码 default:不验证 type:String
spring.auto.datasource.monitor.loginPassword

//default:允许所有用户都可以访问监控中心，填localhost就只允许本地能访问,参数可以是ip，域名，localhost
//type:String
spring.auto.datasource.monitor.allow

//拒绝谁访问，可以ip地址，域名，多个ip，域名用，隔开 default:不拒绝
spring.auto.datasource.monitor.deny

//过滤器拦截地址 default:/* type:String
spring.auto.datasource.monitor.filterPatten

//配置那些地址不拦截 default:*.js,*.gif,*.jpg,*.css,*.png,*.scss,*.bmp,*.mp4,/druid/* type:String
spring.auto.datasource.monitor.exclusions
````

