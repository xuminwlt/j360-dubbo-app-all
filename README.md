## j360-dubbo-app-all

参考《阿里巴巴开发规范1.0》的基于dubbo的rpc案例的完整工程

基于dubbo的分布式工程使用demo,内容未完全完成,逐步补充

### 版本记录:

#### version: 1.0 

1. 基础功能扩展

#### version: 1.1 

1. batch批处理
2. trace链路跟踪功能集成
3. retry重试机制

#### version: 1.2 

1. cache with Redission
2. Guava cache


### 目的

该工程主要围绕基于分布式DUBBO框架的**业务开发**中的开发规范定义,包括module、分包、类的抽象、配置文件、集成开发测试、分布式链路收集等,
不涉及到中间件内部的使用规范,不涉及性能优化,不涉及大数据框架安装配置等。


### module组成

工程中的module按照实际的需要进行拆分,分必要和可选模块,其他配套工程不在此列,详见说明链接

- modules 集成通用型工具模块或其他独立依赖模块
- base 本工程下的抽象底层依赖
- api 对外接口模块
- biz 接口实现模块
- dao 数据访问模块
- web web集成模块
- sjdbc (可选)基于sjdbc的分表分库+读写分离模块,需要注意sql支持列表
- trace (可选)基于brave定制的分布式跟踪集成模块
- hytrix (可选)提供基于hytrix的隔离降级断路策略支持
- tcc (可选)提供基于tcc的分布式最终一致性事务支持
- springfox (可选)基于web的接口文档化工具
- autotest (可选)自动化测试模块
- client (可选)springmvc的快速集成sample,用于演示上述分布式环境提供案例
- batch (可选) 使用spring-batch实现微型批处理,用于批处理作业需求
- redission (可选) 使用redission实现的Redis缓存方案工具


### 规范说明

1. 分层

根据阿里巴巴开发手册定义,按照功能层面分层在工程中的实现为:

- web   
- service
- manager
- dao

2. 分包

分包的规则主要体现在api模块,对应的包的类在工程中的作用

j360-dubbo-api

- constant 定义的接口级别的静态对象
- param 上下层传递的参数,使用DTO结尾,序列化
- query web向service、manager层传递的参数,使用Query结尾,序列化
- result service层返回的对象,可以对DTO进行二次封装,序列化

详细请参考阿里巴巴开发手册1.0 code翻译工程:j360-protocol


### 基于微服务的单个业务的服务形态

- 二方库的规范
- 实现服务的返回定义
- 异常的设计
- 服务层中事务的控制

### 三方库依赖

- ID生成策略,请依赖或者远程请求ID服务器,参考 https://github.com/xuminwlt/j360-idgen
- druid sjdbc1.5.0+不再依赖druid连接池时
- hytrix 使用时,需要配置对应的线程池参数,Filter已经激活,无需配置
- brave 4.2.0 (4.4.0变更太大,暂不更新)


### 链路跟踪

链路跟踪依赖brave底层定制部分埋点设置,对于Event事件不做处理,本地日志通过注入traceId spanId输出,对于跨线程等异步事件不做直接支持,通过侵入式的api调用提供支持。
跨进程调用比如RPC通过上下文将信息进行传递。

HTTP Servlet Filter基于AntPath命名规范的跟踪许可配置
Dubbo Filter TraceId会依赖Http Servlet的traceId存在是否,不存在则生成一个

> 目前该配置在会员系统运行良好,详见modules -> j360-dubbo-trace、j360-dubbo-web

链路过程如下: 

1. 采集到zipkin.log 见: https://github.com/xuminwlt/j360-dubbo-app-all
2. 收集zipkin.log到kafka 见:flume
3. 读取kafka数据到elasticsearch 见: https://github.com/xuminwlt/j360-zipkin-parent

> 并发跟踪支持,使用InheritableThreadLocal继承ThreadLocal特性实现Executor的并发异步执行跟踪,提升吞吐量


### 分表分库/读写分离

sharding-jdbc 1.5.1+
https://github.com/dangdangdotcom/sharding-jdbc

基础功能如下:
1. hash分、range分、时间分
2. 分布式id
3. 单一事务类型
4. 读写分离,强制master

### TCC分布式事务

用户支付场景的使用,rpc适配器需要通过dubbo filter

1. try
2. commit
3. cancel

> tcc实现详见: https://github.com/xuminwlt/j360-tcc

> 简单的rpc实现: https://github.com/xuminwlt/j360-rpc

> 尽量使用单一环境下的本地JDBC事务,除特殊需求(支付等),尽量不使用事务提交

### DMP数据采集

客户端用户行为数据采集及分析

1. 客户端上报用户行为数据到服务端dmp.log
2. 收集dmp.log到kafka  见: flume
3. spark-streaming实时分析 见: https://github.com/xuminwlt/j360-spark-parent
4. kafka到hadoop, spark离线分析


### Hytrix隔离/降级/熔断

1. 增加熔断配置(暂无降级方案)
2. 无隔离配置


### Dubbo Result封装

- 异常收集
- Log输出
- 实例封装

### Web Result封装

- 异常收集
- Log输出
- 实例封装


### 并发线程池方案

1. guava异步线程池
2. Future线程池

- guava EventBus封装ExceptionHandler,解决线程异常时的处理机制
- 在ThreadFactory中不能封装SafeRunnable,否则异常不会上报
- 在单独的线程池中的设定时,需要包装Runnable为SafeRunnable,否则可能会导致整个线程池的崩溃


> 并发容器在进行分布式跟踪时,会丢失对应的跟踪信息,需要配置InheritableThreadLocal将对应的上下文信息写入到对应的线程中
> 并发线程池在执行异步任务时,需要对工作窃取线程池分开配置,便于管理,详见DEMO中:跨rpc并发,jvm内部并发


### 基于Shiro的App客户端认证方案

1. Shiro在工程中的配置未定义到具体类, 详细配置见: https://github.com/xuminwlt/j360-shiro


### Batch批处理

基于Spring batch的微批次处理工程,用于定时的或者数据清洗的批处理作业, Spring batch详见: http://projects.spring.io/spring-batch/

1. 提供文件滚动式输入输出
2. 提供基于数据库(游标、分页2种)的输入输出

### Redis缓存设计

> 基于Redission的缓存设计方案在不同场景中的使用方式

1. Cache-Aside,最重要的缓存方案,如果穿透则到数据源回源
2. Cache-As-SoR(Read-through,Write-through,Write-behind),使用Cache自带的功能进行回源更新,不侵入业务代码,如Guava的load方案

Read-through:读模式
Write-through:同步双写
Write-behind:异步回写模式

sample: 

```

单个:UserRepository.getGoods(Long itemId)

多个:UserRepository.findIn(List<Long> ids)

```

分布式锁

```
lock.tryLock()
```

> 分布式锁使用订阅模式用于通知锁的释放,注意:在codis环境下无法使用,codis不支持订阅

### 高可用

> 使用基于zk选举实现存活监控的高可用,相对分布式锁方案更成熟可靠


### Redis缓存回源方案

使用递增步长+时间步长+定时补偿+MQ补偿形式实现的缓存回源策略
1. MQ补偿：W场景，其中回源过程无限重试，W场景重试一次
2. 定时补偿：在TTL过期范围内，对TTL小于某个值的数据进行集中批量回源
3. 步长模式：在步长达到设定值或者时间达到设定值，同步设定值，并异步回源一次
4. 读写key的操作均会延长TTL

#### W场景

1. 判断是否存在key

1.1 存在，直接递增，进入步长模式

1.2 不存在，回源一次，使用标准setnx获取锁，读取数据库写回redis，其他线程等待一次并重试

1.2.1 重试失败，进入MQ补偿队列

1.2.2 重试成功，同1.1


#### R场景

1. 判断是否存在key

1.1 存在直接返回

1.2 不存在，回源一次，使用标准setnx获取锁，写回redis，其他线程等待一次并重试

1.2.1 重试失败，直接返回0

1.2.2 重试成功，返回值
