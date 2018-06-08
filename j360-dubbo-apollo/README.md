- jar运行

```
java -jar xxx.jar -Denv=dev -Ddev_meta=http://localhost:8080 -DlogRoot=/usr/local/logs
```
- IDEA

Create Main VM输入
-Ddev_meta=http://localhost:8080 -DlogRoot=/usr/local/logs


- 单元测试

```
static {
        System.setProperty("env","dev");
        System.setProperty("dev_meta","http://localhost:8080");
        System.setProperty("logRoot","/usr/local/logs");
    }
```

- Spring Web工程

在容器启动shell后增加 -Ddev_meta=http://localhost:8080 -DlogRoot=/usr/local/logs


## Apollo预研、集成、上线部署流程

