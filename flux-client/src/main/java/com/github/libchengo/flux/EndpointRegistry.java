package com.github.libchengo.flux;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import com.github.libchengo.flux.core.Definition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
public class EndpointRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndpointRegistry.class);

    private static final String METADATA_NAMESPACE = "/flux-metadata";

    private CuratorFramework zkClient;

    private static final String METADATA_DYNAMIC = "+";
    private static final String REQUEST_DYNAMIC_L = "{";
    private static final String REQUEST_DYNAMIC_R = "}";

    private final Decoder decoder;

    public EndpointRegistry(String registryUrl, Decoder decoder) {
        this.decoder = decoder;
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(resolveAddress(registryUrl))
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
    }

    /**
     * 向注册中心注册FxDefinition
     *
     * @param definition FxDefinition
     */
    public void commit(Definition definition) throws Exception {
        // 注册ZK路径，如：/fluxway-metadata/get.api.users.profile
        final String zkPath = makeZkRegisterMetadataPath(definition);
        final byte[] metadata = decoder.decode(definition).getBytes(StandardCharsets.UTF_8);
        if (zkClient.checkExists().forPath(zkPath) == null) {
            final String res = zkClient.create().creatingParentContainersIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(zkPath, metadata);
            LOGGER.info("Metadata register as PrimaryNode[PERSISTENT], path={}, response={}", zkPath, res);
        } else {
            final Stat res = zkClient.setData().forPath(zkPath, metadata);
            LOGGER.info("Metadata register as UpdateNode, path={}, version={}", zkPath, res.getVersion());
        }
    }

    /**
     * Metadata Registry startup
     */
    public void startup() {
        LOGGER.info("Startup... Zookeeper.namespace={}", METADATA_NAMESPACE);
        zkClient.start();
        LOGGER.info("Startup...OK");
    }

    /**
     * Metadata Registry startup
     */
    public void shutdown() {
        LOGGER.info("Cleanup...");
        try {
            zkClient.close();
        } catch (Exception e) {
            LOGGER.warn("Close zookeeper client error:", e);
        }
        LOGGER.info("Cleanup...OK");
    }

    private String makeZkRegisterMetadataPath(Definition definition) {
        final StringBuilder sb = new StringBuilder(METADATA_NAMESPACE)
                .append('/')
                .append(definition.getHttpMethod().toUpperCase());
        final String uri = definition.getHttpUri();
        final String resolved;
        if (uri.contains(REQUEST_DYNAMIC_L) && uri.contains(REQUEST_DYNAMIC_R)) {
            resolved = uri.replaceAll("\\{\\w+\\}", METADATA_DYNAMIC);
        } else {
            resolved = uri;
        }
        return sb.append(resolved.replace('/', '.')).toString();
    }

    private String resolveAddress(String address) {
        return Arrays.stream(address.split(","))
                .map(addr -> {
                    final int idx = addr.indexOf("://");
                    if (idx > 0) {
                        return addr.substring(idx + 3);
                    } else {
                        return addr;
                    }
                })
                .collect(Collectors.joining(","));
    }
}
