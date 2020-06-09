package com.github.libchengo.flux;

import com.github.libchengo.flux.annotation.FxMapping;
import com.github.libchengo.flux.core.MetadataRegistry;
import com.github.libchengo.flux.core.MetadataResolver;
import com.github.libchengo.flux.core.ServiceBeanMetadata;
import org.apache.dubbo.config.spring.ServiceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 * @since 1.0.0
 */
public class SpringBootstrap implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootstrap.class);

    private final MetadataRegistry registry;
    private final MetadataResolver resolver;

    @Value("flux.base-package")
    private String servicePackageName;

    public SpringBootstrap(MetadataRegistry registry, MetadataResolver resolver) {
        this.registry = registry;
        this.resolver = resolver;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LOGGER.info("Flux client discovery start scanning...");
        final Instant start = Instant.now();
        final List<ServiceBeanMetadata> metadata = searchSpringBeans(servicePackageName, event.getApplicationContext());
        try {
            registry.startup();
            registry.submit(metadata.stream()
                    .flatMap(m -> resolver.resolve(m).stream())
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            LOGGER.error("Flux client discovery error: ", e);
        } finally {
            registry.shutdown();
        }
        LOGGER.info("Flux client discovery scan COMPLETED: {}ms", Duration.between(start, Instant.now()));
    }

    private List<ServiceBeanMetadata> searchSpringBeans(String packageName, ApplicationContext context) {
        final boolean filterPackage = !StringUtils.isEmpty(packageName);
        if (filterPackage) {
            LOGGER.info("Flux filter package: {}", packageName);
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
                .map(bean -> new ServiceBeanMetadata(4)
                        .set(ServiceBeanMetadata.META_GROUP, bean.getGroup())
                        .set(ServiceBeanMetadata.META_VERSION, bean.getVersion())
                        .set(ServiceBeanMetadata.META_IFACE_NAME, bean.getInterface())
                        .set(ServiceBeanMetadata.META_IFACE_CLASS, bean.getInterfaceClass())
                )
                .map(metadata -> {
                    final Class<?> iClass = metadata.get(ServiceBeanMetadata.META_IFACE_CLASS);
                    return metadata.set(ServiceBeanMetadata.META_FX_METHODS,
                            Arrays.stream(iClass.getDeclaredMethods())
                                    .filter(m -> m.isAnnotationPresent(FxMapping.class))
                                    .collect(Collectors.toList())
                    );
                }).collect(Collectors.toList());
    }

}
