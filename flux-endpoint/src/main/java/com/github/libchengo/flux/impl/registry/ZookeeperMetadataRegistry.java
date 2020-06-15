package com.github.libchengo.flux.impl.registry;

import com.github.libchengo.flux.core.MetadataDecoder;
import com.github.libchengo.flux.core.MetadataRegistry;
import com.github.libchengo.flux.core.MethodMetadata;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 * @since 1.0.0
 */
public class ZookeeperMetadataRegistry implements MetadataRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperMetadataRegistry.class);

    private final MetadataDecoder decoder;
    private final CuratorFramework zkClient;

    public ZookeeperMetadataRegistry(ZookeeperRegistryConfig config, MetadataDecoder decoder) {
        this.decoder = decoder;
        this.zkClient = CuratorFrameworkFactory.builder()
                .connectString(resolveAddress(config.getAddress()))
                .sessionTimeoutMs(config.getSessionTimeoutMs())
                .connectionTimeoutMs(config.getConnectionTimeoutMs())
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
    }

    @Override
    public void startup() {
        LOGGER.info("Startup... Zookeeper.namespace={}", ROOT_NODE);
        zkClient.start();
        LOGGER.info("Startup...OK");
    }

    @Override
    public void shutdown() {
        LOGGER.info("Cleanup...");
        try {
            zkClient.close();
        } catch (Exception e) {
            LOGGER.warn("Close zookeeper client error:", e);
        }
        LOGGER.info("Cleanup...OK");
    }

    @Override
    public void submit(List<MethodMetadata> metadataList) throws Exception {
        if (metadataList == null || metadataList.isEmpty()) {
            throw new IllegalArgumentException("Method metadata not found");
        }
        for (MethodMetadata metadata : metadataList) {
            final String zkPath = resolveZkPath(metadata);
            final byte[] data = decoder.decode(metadata).getBytes(StandardCharsets.UTF_8);
            if (zkClient.checkExists().forPath(zkPath) == null) {
                final String res = zkClient.create()
                        .creatingParentContainersIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(zkPath, data);
                LOGGER.info("Metadata register as PrimaryNode[PERSISTENT], path={}, response={}", zkPath, res);
            } else {
                final Stat res = zkClient.setData().forPath(zkPath, data);
                LOGGER.info("Metadata register as UpdateNode, path={}, version={}", zkPath, res.getVersion());
            }
        }
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

    private String resolveZkPath(MethodMetadata metadata) {
        // /flux/get/sample.test.$.profile
        final String path = resolveDynamic(metadata.getHttpUri().replace('/', '.'));
        return Paths.get(ROOT_NODE,
                metadata.getHttpMethod().toLowerCase(),
                path).toString();
    }

    private final static Pattern DYNAMIC_PATH = Pattern.compile("(\\{.+\\})");

    private static String resolveDynamic(String path) {
        if ('.' == path.charAt(0)){
            path = path.substring(1);
        }
        return DYNAMIC_PATH.matcher(path).replaceAll("\\$");
    }

}
