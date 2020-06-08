package com.github.libchengo.flux;

import org.apache.dubbo.config.spring.ServiceBean;
import com.github.libchengo.flux.annotation.FxMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 网关注册扫描类
 *
 * @author 陈哈哈 (yongjia-chen@outlook.com)
 */
public class FxDiscovery implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FxDiscovery.class);

    private static final String META_GROUP = "group";
    private static final String META_VERSION = "version";
    private static final String META_IFACE_NAME = "interfaceName";
    private static final String META_IFACE_CLASS = "interfaceClass";
    private static final String META_FX_METHODS = "fxMethods";

    private final FxMetadataConfig metadataConfig;

    private final FxResolver definitionResolver = new FxResolver();

    public FxDiscovery(FxMetadataConfig metadataConfig) {
        this.metadataConfig = metadataConfig;
    }

    @Async
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LOGGER.info("Fluxway discovery start scanning...");
        final Instant start = Instant.now();
        dubboBeansDiscovery(event.getApplicationContext());
        LOGGER.info("Fluxway discovery scan COMPLETED: {}ms", Duration.between(start, Instant.now()));
    }

    private void dubboBeansDiscovery(ApplicationContext context) {
        final String appName = context.getEnvironment().getProperty("spring.application.name");
        final List<BeanMetadata> beans = scanBeans(context);
        LOGGER.info("Fluxway[{}] scan beans: {}", appName, beans.size());
        // 注册到元数据中心
        final FxMetadataRegistry registry = new FxMetadataRegistry(metadataConfig.getAddress(), new FxDecoder());
        registry.startup();
        final String prefix = metadataConfig.getPrefix() == null ? "/" : metadataConfig.getPrefix();
        LOGGER.info("Fluxway client prefix: {}", prefix);
        try {
            beans.forEach(metadata -> {
                final String group = metadata.getOrDefault(META_GROUP, "");
                final String version = metadata.getOrDefault(META_VERSION, "");
                final String interfaceName = metadata.get(META_IFACE_NAME);
                final List<Method> methods = metadata.get(META_FX_METHODS);
                LOGGER.info("Dubbo.Bean: group={}, version={}, iface={}, methods={}", group, version, interfaceName, methods.size());
                methods.stream()
                        .map(method -> definitionResolver.resolve(
                                prefix, appName,
                                group, version,
                                interfaceName,
                                method.getDeclaredAnnotation(FxMapping.class),
                                method
                        ))
                        .forEach(definition -> {
                            LOGGER.info("Fluxway metadata: {} {}", definition.getHttpMethod(), definition.getHttpUri());
                            try {
                                registry.commit(definition);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
            });
        } finally {
            registry.shutdown();
        }
    }

    private List<BeanMetadata> scanBeans(ApplicationContext context) {
        final String packageName = metadataConfig.getBasePackage();
        final boolean filterPackage = !StringUtils.isEmpty(packageName);
        if (filterPackage) {
            LOGGER.info("Fluxway filter package: {}", packageName);
        }
        final Collection<ServiceBean> beans = context.getBeansOfType(ServiceBean.class).values();
        LOGGER.debug("Load dubbo service beans: {}", beans.size());
        return beans.stream()
                .filter(sb -> {
                    if (filterPackage) {
                        return sb.getInterface().startsWith(packageName);
                    } else {
                        return true;
                    }
                })
                .peek(bean -> LOGGER.info("Found dubbo.bean: {}", bean))
                .map(bean -> new BeanMetadata(4)
                        .set(META_GROUP, bean.getGroup())
                        .set(META_VERSION, bean.getVersion())
                        .set(META_IFACE_NAME, bean.getInterface())
                        .set(META_IFACE_CLASS, bean.getInterfaceClass())
                )
                .map(metadata -> {
                    final Class<?> iClass = metadata.get(META_IFACE_CLASS);
                    return metadata.set(META_FX_METHODS,
                            Arrays.stream(iClass.getDeclaredMethods())
                                    .filter(m -> m.isAnnotationPresent(FxMapping.class))
                                    .collect(Collectors.toList())
                    );
                }).collect(Collectors.toList());
    }

    ////

    private static final class BeanMetadata extends HashMap<String, Object> {
        private BeanMetadata(int initialCapacity) {
            super(initialCapacity);
        }

        private BeanMetadata set(String name, Object value) {
            put(name, value);
            return this;
        }

        private <T> T get(String name) {
            return getOrDefault(name, null);
        }

        @SuppressWarnings("unchecked")
        private <T> T getOrDefault(String name, T defaultValue) {
            return (T) super.getOrDefault(name, defaultValue);
        }
    }
}
