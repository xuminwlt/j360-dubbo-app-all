## j360-dubbo-app-all

参考《阿里巴巴开发规范1.0》的基于dubbo的rpc案例的完整工程

基于dubbo的分布式工程使用demo,内容未完全完成,逐步补充


### 目的

该工程主要围绕基于分布式DUBBO框架的**业务开发**中的开发规范定义,包括module、分包、类的抽象、配置文件、集成开发测试、分布式链路收集等,
不涉及到中间件内部的使用规范,不涉及性能优化,不涉及大数据框架安装配置等。


### module组成

工程中的module按照实际的需要进行拆分,分必要和可选模块

- modules 集成通用型工具模块或其他独立依赖模块
- base 本工程下的抽象底层依赖
- api 对外接口模块
- biz 接口实现模块
- dao 数据访问模块
- web web集成模块
- sjdbc (可选)基于sjdbc的分表分库+读写分离模块,需要注意sql支持列表,不含id生成策略
- trace (可选)基于brave定制的分布式跟踪集成模块
- hytrix (可选)提供基于hytrix的隔离降级断路策略支持
- tcc (可选)提供基于tcc的分布式最终一致性事务支持
- springfox (可选)基于web的接口文档化工具
- autotest (可选)自动化测试模块


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
- druid sjdbc依赖druid连接池时,对版本有要求,请勿修改druid version
- hytrix 使用时,需要配置对应的线程池参数,Filter已经激活,无需配置
- brave 4.2.0


### 链路跟踪

链路跟踪依赖brave底层定制部分埋点设置,对于Event事件不做处理,本地日志通过注入traceId spanId输出,对于跨线程等异步事件不做直接支持,通过侵入式的api调用提供支持。
跨进程调用比如RPC通过上下文将信息进行传递。

HTTP Servlet Filter基于AntPath命名规范的跟踪许可配置
Dubbo Filter TraceId会依赖Http Servlet的traceId存在是否,不存在则生成一个

