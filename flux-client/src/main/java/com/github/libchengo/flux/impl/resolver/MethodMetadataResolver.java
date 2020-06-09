package com.github.libchengo.flux.impl.resolver;

import com.github.libchengo.flux.annotation.FxMapping;
import com.github.libchengo.flux.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 * @since 1.0.0
 */
public class MethodMetadataResolver implements MetadataResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodMetadataResolver.class);

    private final List<ParameterResolver> parameterResolvers = new ArrayList<>();

    public MethodMetadataResolver() {
        final EndpointHelper helper = new EndpointHelper();
        parameterResolvers.add(new EndpointParameterResolver(helper));
        parameterResolvers.add(new ObjectParameterResolver(helper));
    }

    @Override
    public List<MethodMetadata> resolve(ServiceBeanMetadata metadata) {
        final String prefix = metadata.getOrDefault(ServiceBeanMetadata.META_PREFIX, "");
        final String appName = metadata.getOrDefault(ServiceBeanMetadata.META_APP_NAME, "");
        final String group = metadata.getOrDefault(ServiceBeanMetadata.META_GROUP, "");
        final String version = metadata.getOrDefault(ServiceBeanMetadata.META_VERSION, "");
        final String interfaceName = metadata.get(ServiceBeanMetadata.META_IFACE_NAME);
        final List<Method> methods = metadata.get(ServiceBeanMetadata.META_FX_METHODS);
        LOGGER.info("Dubbo.Bean: group={}, version={}, iface={}, methods={}", group, version, interfaceName, methods.size());
        return methods.stream()
                .map(method -> resolve(prefix, appName,
                        group, version, interfaceName, method.getDeclaredAnnotation(FxMapping.class), method))
                .collect(Collectors.toList());
    }

    public MethodMetadata resolve(String prefix, String appName,
                                  String serviceGroup, String serviceVer, String interfaceName, FxMapping mapping, Method method) {
        final MethodMetadata.Builder builder = MethodMetadata.builder()
                .application(appName == null ? "" : appName)
                .rpcGroup(serviceGroup == null ? "" : serviceGroup)
                .rpcVersion(serviceVer == null ? "" : serviceVer);
        final String path = mapping.path();
        final String group = mapping.group();
        final String version = mapping.version();
        if (!group.isEmpty()) {
            builder.rpcGroup(group);
        }
        if (!version.isEmpty()) {
            builder.rpcVersion(version);
        }
        builder.protocol(SupportProtocol.DUBBO);
        builder.authorized(mapping.authorized());
        // 网关侧请求定义
        builder.httpUri(Paths.get(prefix, path).toString());
        builder.httpMethod(mapping.method().name());
        // 后端目标请求定义
        builder.serviceUri(interfaceName);
        builder.serviceMethod(method.getName());
        // 解析方法参数类型
        final int count = method.getParameterCount();
        final List<ParameterMetadata> parameterMetadata = new ArrayList<>(count);
        if (count > 0) {
            final java.lang.reflect.Parameter[] mps = method.getParameters();
            final Type[] gts = method.getGenericParameterTypes();
            for (int i = 0; i < count; i++) {
                ParameterMetadata field = null;
                for (ParameterResolver resolver : parameterResolvers) {
                    field = resolver.resolve(mps[i], gts[i]);
                    if (field != null) {
                        break;
                    }
                }
                if (field == null) {
                    throw new IllegalArgumentException("无法解析的方法参数:" + method +
                            ", parameter=" + mps[i] +
                            ", generic=" + gts[i]
                    );
                } else {
                    parameterMetadata.add(field);
                }
            }
        }
        builder.parameters(parameterMetadata);
        return builder.build();
    }
}
