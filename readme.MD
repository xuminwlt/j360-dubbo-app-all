## j360-dubbo-app-all

参考《阿里巴巴开发规范1.0》的基于dubbo的rpc案例的完整工程
基于dubbo的分布式工程使用demo,内容未完全完成,逐步补充

### module组成

工程中的module按照实际的需要进行拆分,分必要和可选模块

- modules:
- base:
- api:
- biz:
- dao:
- web:
- trace:(可选)
- springfox:(可选)
- autotest:(可选)


### 规范说明

1. 分层

2. 分包


详细请参考阿里巴巴开发手册1.0 code翻译工程:j360-protocol


### 基于微服务的单个业务的服务形态

- 对包暴露dubbo rpc接口
- 拦截所有业务异常
