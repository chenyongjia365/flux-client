package net.bytepowereded.flux.impl.resolver;

import net.bytepowereded.flux.core.ParameterMetadata;
import net.bytepowereded.flux.core.ParameterType;
import net.bytepowered.flux.annotation.*;
import net.bytepowereded.flux.annotation.*;

import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
public class EndpointHelper {

    public EndpointHelper() {
    }

    public ParameterMetadata create(AnnotatedElement element,
                                    String className, List<String> genericTypes,
                                    String fieldName, String defaultHttpName) {
        final FxScope scope;
        final String httpName;
        if (element.isAnnotationPresent(FxAttr.class)) {
            final FxAttr attr = element.getAnnotation(FxAttr.class);
            scope = FxScope.ATTR;
            httpName = aliasFor(attr.name(), attr.value());
        } else if (element.isAnnotationPresent(FxAttrs.class)) {
            scope = FxScope.ATTRS;
            httpName = "$attrs";
        } else if (element.isAnnotationPresent(FxForm.class)) {
            final FxForm param = element.getAnnotation(FxForm.class);
            scope = FxScope.FORM;
            httpName = aliasFor(param.name(), param.value());
        } else if (element.isAnnotationPresent(FxHeader.class)) {
            final FxHeader header = element.getAnnotation(FxHeader.class);
            scope = FxScope.HEADER;
            httpName = aliasFor(header.name(), header.value());
        } else if (element.isAnnotationPresent(FxParam.class)) {
            final FxParam attr = element.getAnnotation(FxParam.class);
            scope = FxScope.PARAM;
            httpName = aliasFor(attr.name(), attr.value());
        } else if (element.isAnnotationPresent(FxPath.class)) {
            final FxPath path = element.getAnnotation(FxPath.class);
            scope = FxScope.PATH;
            httpName = aliasFor(path.name(), path.value());
        } else if (element.isAnnotationPresent(FxQuery.class)) {
            final FxQuery query = element.getAnnotation(FxQuery.class);
            scope = FxScope.QUERY;
            httpName = aliasFor(query.name(), query.value());
        } else {
            scope = FxScope.AUTO;
            httpName = defaultHttpName;
        }
        return ParameterMetadata.builder()
                .element(element)
                .className(className)
                .genericTypes(genericTypes)
                .fieldName(fieldName)
                .httpName(fixHttpName(httpName, defaultHttpName))
                .httpScope(scope)
                .type(ParameterType.PARAMETER)
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
        if (paramType.isPrimitive()) {
            throw new IllegalArgumentException("必须使用基础数据类型的包装类型(BoxedType): " + paramType);
        }
        // 必须是JavaRuntime的基础类
        if (!paramType.getCanonicalName().startsWith("java.")) {
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
