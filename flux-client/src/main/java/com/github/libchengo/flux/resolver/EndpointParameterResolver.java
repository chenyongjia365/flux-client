package com.github.libchengo.flux.resolver;

import com.github.libchengo.flux.core.Parameter;
import com.github.libchengo.flux.core.ParameterResolver;

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
    public Parameter resolve(java.lang.reflect.Parameter parameter, Type genericType) {
        if (!endpoint.isEndpointType(parameter.getType())) {
            return null;
        }
        final GenericHelper generic = GenericHelper.from(parameter, genericType);
        return endpoint.create(
                parameter,
                generic.className,
                generic.genericTypes,
                parameter.getName(),
                parameter.getName());
    }

}
