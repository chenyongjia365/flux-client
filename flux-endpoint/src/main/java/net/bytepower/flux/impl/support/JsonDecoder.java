package net.bytepower.flux.impl.support;

import net.bytepower.flux.core.MetadataDecoder;
import net.bytepower.flux.core.MethodMetadata;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 * @since 1.0.0
 */
public class JsonDecoder implements MetadataDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonDecoder.class);

    private final Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .registerTypeAdapter(Annotation.class, new AnnotationAdapter())
            .registerTypeAdapter(Class.class, new ClassAdapter())
            .create();

    @Override
    public String decode(MethodMetadata metadata) {
        return gson.toJson(metadata);
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
