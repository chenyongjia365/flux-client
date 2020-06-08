package com.github.libchengo.flux.resolver;

import com.github.libchengo.flux.core.FxParameter;
import com.github.libchengo.flux.core.FxParameterResolver;
import com.github.libchengo.flux.core.FxParameterType;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 陈哈哈 (yongjia-chen@outlook.com)
 */
public class ObjectParameterResolver implements FxParameterResolver {

    private final EndpointHelper endpoint;

    public ObjectParameterResolver(EndpointHelper endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public FxParameter resolve(java.lang.reflect.Parameter parameter, Type genericType) {
        final Class<?> parameterType = parameter.getType();
        if (!isPojoType(parameterType)) {
            return null;
        }
        // POJO类型：不处理泛型
        final String className = parameter.getType().getTypeName();
        return FxParameter.builder()
                .className(className)
                .genericTypes(Collections.emptyList())
                .fieldName(parameter.getName())
                .type(FxParameterType.POJO_OBJECT)
                .fields(Stream.of(parameterType.getDeclaredFields())
                        .map(this::makeValueFieldFromPojoField)
                        .collect(Collectors.toList()))
                .build();
    }


    private FxParameter makeValueFieldFromPojoField(Field field) {
        final Class<?> fieldType = field.getType();
        if (!endpoint.isEndpointType(fieldType)) {
            throw new IllegalArgumentException("POJO的成员属性字段，必须是有效的数值端点属性");
        }
        final GenericHelper generic = GenericHelper.from(field);
        return endpoint.create(
                field,
                generic.className,
                generic.genericTypes,
                field.getName(),
                field.getName());
    }

    /**
     * 判断是否为POJO类型
     * 判断标准：每个成员变量，都包含对应的Getter/Setter方法
     */
    private boolean isPojoType(Class<?> paramType) {
        final Field[] fields = paramType.getDeclaredFields();
        if (fields.length == 0) {
            return false;
        }
        // 每个成员变量，都包含对应的Getter/Setter方法
        return Stream.of(fields)
                .map(Field::getName)
                .map(f -> "get" + Character.toUpperCase(f.charAt(0)) + f.substring(1))
                .allMatch(m -> {
                    try {
                        paramType.getDeclaredMethod(m);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                });
    }
}
