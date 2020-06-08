package com.github.libchengo.flux;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
@Component
@ConfigurationProperties(prefix = "flux.endpoint-registry")
public class EndpointConfig {

    /**
     * 统一API前缀
     */
    private String prefix;

    /**
     * 扫描目录
     */
    private String basePackage;

    /**
     * 注册中心地址
     */
    private String address;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
