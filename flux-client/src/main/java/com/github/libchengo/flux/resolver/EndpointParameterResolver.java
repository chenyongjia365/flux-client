package com.github.libchengo.flux.resolver;

import com.github.libchengo.flux.core.FxParameter;
import com.github.libchengo.flux.core.FxParameterResolver;

import java.lang.reflect.Type;

/**
 * @author 陈哈哈 (yongjia-chen@outlook.com)
 */
public class EndpointParameterResolver implements FxParameterResolver {

    private final EndpointHelper endpoint;

    public EndpointParameterResolver(EndpointHelper endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public FxParameter resolve(java.lang.reflect.Parameter parameter, Type genericType) {
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
