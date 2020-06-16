package net.bytepower.flux.impl.registry;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
public class ZookeeperRegistryConfig {

    /**
     * Session超时：毫秒
     */
    private final int sessionTimeoutMs;

    /**
     * 连接超时：毫秒
     */
    private final int connectionTimeoutMs;

    /**
     * 注册中心地址列表
     */
    private final String address ;

    public ZookeeperRegistryConfig(int sessionTimeoutMs, int connectionTimeoutMs, String address) {
        this.sessionTimeoutMs = sessionTimeoutMs;
        this.connectionTimeoutMs = connectionTimeoutMs;
        this.address = address;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public String getAddress() {
        return address;
    }
}
