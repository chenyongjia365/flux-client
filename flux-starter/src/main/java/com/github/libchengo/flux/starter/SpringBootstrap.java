package com.github.libchengo.flux.starter;

import com.github.libchengo.flux.annotation.FxMapping;
import com.github.libchengo.flux.core.MetadataRegistry;
import com.github.libchengo.flux.core.MetadataResolver;
import com.github.libchengo.flux.core.ServiceBeanMetadata;
import org.apache.dubbo.config.spring.ServiceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final SpringFluxConfig config;
    private final MetadataRegistry registry;
    private final MetadataResolver resolver;

    public SpringBootstrap(SpringFluxConfig config, MetadataRegistry registry, MetadataResolver resolver) {
        this.config = config;
        this.registry = registry;
        this.resolver = resolver;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LOGGER.info("Flux client discovery start scanning...");
        final Instant start = Instant.now();
        final List<ServiceBeanMetadata> metadata = searchMappingBeans(event.getApplicationContext());
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

    private List<ServiceBeanMetadata> searchMappingBeans(ApplicationContext context) {
        final List<String> packages = scanPackages();
        if (packages.isEmpty()) {
            return searchPackageBeans(null, context);
        } else {
            if (packages.size() == 1) {
                return searchPackageBeans(packages.get(0), context);
            } else {
                return packages.stream().parallel()
                        .flatMap(pack -> searchPackageBeans(pack, context).stream())
                        .collect(Collectors.toList());
            }
        }
    }

    private List<ServiceBeanMetadata> searchPackageBeans(String packageName, ApplicationContext context) {
        final boolean filterPackage = !StringUtils.isEmpty(packageName);
        if (filterPackage) {
            LOGGER.info("Flux filter package: {}", packageName);
        }
        final String prefix = config.getPrefix();
        final String applicationName = context.getApplicationName();
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
                .map(bean -> ServiceBeanMetadata.builder()
                        .application(applicationName)
                        .prefix(prefix)
                        .group(bean.getGroup())
                        .version(bean.getVersion())
                        .interfaceName(bean.getInterface())
                        .interfaceClass(bean.getInterfaceClass())
                        .methods(Arrays.stream(bean.getInterfaceClass().getDeclaredMethods())
                                .filter(m -> m.isAnnotationPresent(FxMapping.class))
                                .collect(Collectors.toList()))
                        .build()
                ).collect(Collectors.toList());
    }

    private List<String> scanPackages() {
        return Arrays.stream(config.getBasePackage().split(","))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
