package com.github.libchengo.flux.resolver;

import com.github.libchengo.flux.annotation.*;
import com.github.libchengo.flux.core.FxHttpScope;
import com.github.libchengo.flux.core.FxParameter;
import com.github.libchengo.flux.core.FxParameterType;
import org.apache.dubbo.common.utils.ReflectUtils;

import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author 陈哈哈 (yongjia-chen@outlook.com)
 */
public class EndpointHelper {

    public EndpointHelper() {
    }

    public FxParameter create(AnnotatedElement element,
                              String className, List<String> genericTypes,
                              String fieldName, String defaultHttpName) {
        final FxHttpScope httpScope;
        final String httpName;
        if (element.isAnnotationPresent(FxPath.class)) {
            final FxPath path = element.getAnnotation(FxPath.class);
            httpScope = FxHttpScope.PATH;
            httpName = aliasFor(path.name(), path.value());
        } else if (element.isAnnotationPresent(FxHeader.class)) {
            final FxHeader header = element.getAnnotation(FxHeader.class);
            httpScope = FxHttpScope.HEADER;
            httpName = aliasFor(header.name(), header.value());
        } else if (element.isAnnotationPresent(FxRequest.class)) {
            final FxRequest param = element.getAnnotation(FxRequest.class);
            httpScope = FxHttpScope.PARAM;
            httpName = aliasFor(param.name(), param.value());
        } else if (element.isAnnotationPresent(FxAttr.class)) {
            final FxAttr attr = element.getAnnotation(FxAttr.class);
            httpScope = FxHttpScope.ATTR;
            httpName = aliasFor(attr.name(), attr.value());
        } else if (element.isAnnotationPresent(FxAttributes.class)) {
            httpScope = FxHttpScope.ATTRIBUTES;
            httpName = "$attrs";
        } else {
            httpScope = FxHttpScope.AUTO;
            httpName = defaultHttpName;
        }
        return FxParameter.builder()
                .element(element)
                .className(className)
                .genericTypes(genericTypes)
                .fieldName(fieldName)
                .httpName(fixHttpName(httpName, defaultHttpName))
                .httpScope(httpScope)
                .type(FxParameterType.PARAMETER)
                .build();
    }

    /**
     * 判断参数类型，是否为支持的端点数值类型。
     * 前提条件：必须是Java运行时内置数据类型：即可以被网关运行时加载；
     * 支持的类型：
     * 1. Java原生类型的包装类型，不支持int,long,boolean等非包装类型；
     * 2. 容器类型，只支持Map、Collection接口类型；
     * 3. Java运行时其它类型，必须支持valueOf(String), parse(String)静态方法解析；
     *
     * @param paramType 参数类型
     * @return 是否值类型字段
     */
    public boolean isEndpointType(Class<?> paramType) {
        // 1. Java原生类型数据类型
        if (ReflectUtils.isPrimitive(paramType)) {
            if (paramType.equals(ReflectUtils.getBoxedClass(paramType))) {
                return true;
            } else {
                throw new IllegalArgumentException("必须使用基础数据类型的包装类型(BoxedType): " + paramType);
            }
        }
        // 必须是JavaRuntime的基础类
        if (!paramType.getCanonicalName().startsWith("java")) {
            return false;
        }
        // 容器类型
        if (Map.class.isAssignableFrom(paramType) || Collection.class.isAssignableFrom(paramType)) {
            return true;
        } else {
            // 其它：需要静态解析方法:
            // 1. public static T valueOf(String)
            // 1. public static T parse(String)
            final boolean match = Stream.of("valueOf", "parse")
                    .map(n -> {
                        try {
                            return paramType.getMethod(n, String.class);
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .anyMatch(m -> m != null && paramType.equals(m.getReturnType()));
            if (!match) {
                throw new IllegalArgumentException("类型不存在静态解析方法(valueOf(String)/parse(String)): " + paramType);
            }
            return true;
        }
    }

    private String aliasFor(String nameOfNameMethod, String nameOfValueMethod) {
        if (nameOfNameMethod == null || nameOfNameMethod.isEmpty()) {
            return nameOfValueMethod;
        } else {
            return nameOfNameMethod;
        }
    }

    private String fixHttpName(String httpName, String defaultHttpName) {
        return (httpName == null || httpName.isEmpty()) ? defaultHttpName : httpName;
    }
}
