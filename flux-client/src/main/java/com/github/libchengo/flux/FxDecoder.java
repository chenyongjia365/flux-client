package com.github.libchengo.flux;

import com.google.gson.*;
import com.github.libchengo.flux.core.FxDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author 陈哈哈 (yongjia-chen@outlook.com)
 */
public class FxDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(FxDecoder.class);

    private final Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .registerTypeAdapter(Annotation.class, new AnnotationAdapter())
            .registerTypeAdapter(Class.class, new ClassAdapter())
            .create();

    /**
     * 序列化对象为JSON字符串
     *
     * @param mapper RequestMapper
     * @return JSON 字符串
     */
    public String decode(FxDefinition mapper) {
        return gson.toJson(mapper);
    }

    ////

    private static class ClassAdapter implements JsonSerializer<Class<?>> {

        @Override
        public JsonElement serialize(Class aClass, Type type, JsonSerializationContext ctx) {
            return new JsonPrimitive(aClass.getCanonicalName());
        }
    }

    private static class AnnotationAdapter implements JsonSerializer<Annotation> {

        @Override
        public JsonElement serialize(Annotation annotation, Type type, JsonSerializationContext ctx) {
            final JsonObject json = new JsonObject();
            final Class<? extends Annotation> clazz = annotation.annotationType();
            json.addProperty("class", clazz.getCanonicalName());
            for (Method method : clazz.getDeclaredMethods()) {
                final Object value;
                try {
                    value = method.invoke(annotation);
                } catch (Exception e) {
                    LOGGER.error("--> Invoke annotation.method error:", e);
                    continue;
                }
                // 注解的方法名和数值
                final String name = method.getName();
                final Class<?> valueType = method.getReturnType();
                json.add(name, ctx.serialize(value, valueType));
                json.addProperty(name + ".returnType", valueType.getCanonicalName());
            }
            return json;
        }
    }
}
