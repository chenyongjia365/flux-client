# Flux Client - Flux网关Java接入客户端

## 使用

在需要暴露给网关的Dubbo接口上定义相关注解

```java
@FxMapping(path = "/test/complex", method = FxMethod.GET, authorized = false)
FxResponse hello(
        @FxAttr("fluxgo.request_id") String requestId,
        @FxRequest(value = "group") Integer group,
        List<Integer> state
);
```

## 配置

### Dubbo 配置中心

```yaml
# Apache Dubbo配置
dubbo:
  application:
    name: "${spring.application.name}"
  registry:
    address: zookeeper://tx.devserver.net:2181
  scan:
    base-packages: "com.github.libchengo.flux.sample.service"
```

### Flux网关元数据配置

```yaml
# ZK 作为Flux网关Http与后端Dubbo请求映射的元数据中心
flux:
  methodMetadata-registry:
    address: zookeeper://tx.devserver.net:2181
    base-package: "com.github.libchengo.flux.sample.service"

```

## 示例

详细见本项目的 flux-sample 示例。

## LICENSE

[Apache License 2.0](LICENSE)