package com.github.libchengo.flux;

import com.github.libchengo.flux.annotation.FxMapping;
import com.github.libchengo.flux.core.FxDefinition;
import com.github.libchengo.flux.core.FxParameter;
import com.github.libchengo.flux.core.FxParameterResolver;
import com.github.libchengo.flux.core.FxProtocol;
import com.github.libchengo.flux.resolver.EndpointHelper;
import com.github.libchengo.flux.resolver.EndpointParameterResolver;
import com.github.libchengo.flux.resolver.ObjectParameterResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈哈哈 (yongjia-chen@outlook.com)
 */
public class FxResolver {

    private final List<FxParameterResolver> resolvers = new ArrayList<>();

    public FxResolver() {
        final EndpointHelper helper = new EndpointHelper();
        resolvers.add(new EndpointParameterResolver(helper));
        resolvers.add(new ObjectParameterResolver(helper));
    }

    public FxDefinition resolve(String prefix, String appName,
                                String serviceGroup, String serviceVer, String interfaceName, FxMapping mapping, Method method) {
        final FxDefinition.Builder builder = FxDefinition.builder()
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
        builder.protocol(FxProtocol.DUBBO);
        builder.authorized(mapping.authorized());
        // 网关侧请求定义
        builder.httpUri(Paths.get(prefix, path).toString());
        builder.httpMethod(mapping.method().name());
        // 后端目标请求定义
        builder.serviceUri(interfaceName);
        builder.serviceMethod(method.getName());
        // 解析方法参数类型
        final int count = method.getParameterCount();
        final List<FxParameter> parameters = new ArrayList<>(count);
        if (count > 0) {
            final java.lang.reflect.Parameter[] mps = method.getParameters();
            final Type[] gts = method.getGenericParameterTypes();
            for (int i = 0; i < count; i++) {
                FxParameter field = null;
                for (FxParameterResolver resolver : resolvers) {
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
                    parameters.add(field);
                }
            }
        }
        builder.parameters(parameters);
        return builder.build();
    }

}
