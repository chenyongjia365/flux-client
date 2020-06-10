package com.github.libchengo.flux.starter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yongjia.chen
 * @since 1.0.0
 */
@Component
@ConfigurationProperties("flux")
public class SpringFluxConfig {

    @Value("${metadata-registry.session-timeout-ms:30000}")
    private int sessionTimeoutMs;

    @Value("${metadata-registry.connection-timeout-ms:60000}")
    private int connectionTimeoutMs;

    @Value("${metadata-registry.address:30000}")
    private String address;

    @Value("${base-package:\"\"}")
    private String basePackage;

    @Value("${base-package:\"\"}")
    private String prefix;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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
}
