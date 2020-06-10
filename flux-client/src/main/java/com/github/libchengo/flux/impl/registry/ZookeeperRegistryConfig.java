package com.github.libchengo.flux.impl.registry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
@Component
@ConfigurationProperties(prefix = "flux.metadata-registry")
public class ZookeeperRegistryConfig {

    /**
     * Session超时：毫秒
     */
    @Value("${session-timeout-ms:10000}")
    private int sessionTimeoutMs;

    /**
     * 连接超时：毫秒
     */
    @Value("${session-timeout-ms:30000}")
    private int connectionTimeoutMs;

    /**
     * 注册中心地址列表
     */
    @Value("${address:zookeeper://zookeeper:2181}")
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }
}
