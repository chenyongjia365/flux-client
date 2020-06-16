package net.bytepowered.flux.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yongjia.chen
 * @since 1.0.0
 */
@Component
@ConfigurationProperties("flux.metadata-registry")
public class SpringRegistryConfig {

    private int sessionTimeoutMs = 30_000;
    private int connectionTimeoutMs = 60_000;
    private String address = "zookeeper://zookeeper:2181";

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

}
