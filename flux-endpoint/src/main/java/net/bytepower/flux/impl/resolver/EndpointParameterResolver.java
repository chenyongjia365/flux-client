package net.bytepower.flux.impl.resolver;

import net.bytepower.flux.core.ParameterMetadata;
import net.bytepower.flux.core.ParameterResolver;

import java.lang.reflect.Type;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
public class EndpointParameterResolver implements ParameterResolver {

    private final EndpointHelper endpoint;

    public EndpointParameterResolver(EndpointHelper endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public ParameterMetadata resolve(java.lang.reflect.Parameter parameter, Type genericType) {
        if (!endpoint.isEndpointType(parameter.getType())) {
            return null;
        }
        final GenericTypeHelper generic = GenericTypeHelper.from(parameter, genericType);
        return endpoint.create(
                parameter,
                generic.className,
                generic.genericTypes,
                parameter.getName(),
                parameter.getName());
    }

}
