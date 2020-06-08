package com.github.libchengo.flux.resolver;

import org.apache.dubbo.common.utils.ReflectUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author 陈哈哈 yongjia-chen@outlook.com
 */
public class GenericHelper {

    public final String className;
    public final List<String> genericTypes;
    public final boolean isGenericType;
    public final boolean isCollectionType;
    public final boolean isMapType;

    private GenericHelper(String className, List<String> genericTypes, boolean isGenericType, boolean isCollectionType, boolean isMapType) {
        this.className = className;
        this.genericTypes = genericTypes;
        this.isGenericType = isGenericType;
        this.isCollectionType = isCollectionType;
        this.isMapType = isMapType;
    }

    public static GenericHelper from(Parameter parameter, Type genericType) {
        return from(parameter.getType(), genericType);
    }

    public static GenericHelper from(Field field) {
        return from(field.getType(), field.getGenericType());
    }

    private static GenericHelper from(Class<?> classType, Type genericType) {
        boolean isCollectionType = Collection.class.isAssignableFrom(classType);
        boolean isMapType = Map.class.isAssignableFrom(classType);
        final List<String> genericTypes = new ArrayList<>(2);
        if (!isCollectionType && !isMapType) {
            return new GenericHelper(
                    classType.getCanonicalName(),
                    genericTypes,
                    false, false, false
            );
        }
        if (!classType.isInterface()) {
            throw new IllegalArgumentException("参数为容器类型时，其类型必须是<Interface>: " + classType);
        }
        final ParameterizedType pt = (ParameterizedType) genericType;
        final Class<?> primaryArg = (Class<?>) pt.getActualTypeArguments()[0];
        final String notSerializableTypeError = "参数为容器类型时，其泛型类型必须是<PrimitiveType>或<Serializable>: " + genericType;
        if (isCollectionType) {
            if (!isSerializableType(primaryArg)) {
                throw new IllegalArgumentException(notSerializableTypeError);
            }
        }
        genericTypes.add(primaryArg.getCanonicalName());
        if (isMapType) {
            final Class<?> secondArg = (Class<?>) pt.getActualTypeArguments()[1];
            if (!isSerializableType(primaryArg) || !isSerializableType(secondArg)) {
                throw new IllegalArgumentException(notSerializableTypeError);
            }
            genericTypes.add(secondArg.getCanonicalName());
        }
        return new GenericHelper(
                classType.getCanonicalName(),
                genericTypes,
                !genericTypes.isEmpty(),
                isCollectionType,
                isMapType
        );
    }

    private static boolean isSerializableType(Class<?> clazz) {
        return ReflectUtils.isPrimitive(clazz) || clazz.isAssignableFrom(Serializable.class);
    }
}
