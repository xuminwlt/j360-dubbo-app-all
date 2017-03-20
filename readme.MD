## j360-dubbo-app-all

参考《阿里巴巴开发规范1.0》的基于dubbo的rpc案例的完整工程

基于dubbo的分布式工程使用demo,内容未完全完成,逐步补充

### module组成

工程中的module按照实际的需要进行拆分,分必要和可选模块

- modules 集成通用型工具模块或其他独立依赖模块
- base 本工程下的抽象底层依赖
- api 对外接口模块
- biz 接口实现模块
- dao 数据访问模块
- web web集成模块
- sjdbc 基于sjdbc的分表分库+读写分离模块
- trace (可选)分布式跟踪集成模块
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

