package com.github.libchengo.flux.core;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.util.List;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
public class Parameter implements Serializable {

    private transient AnnotatedElement element;

    /**
     * 字段数据类型的class名称
     */
    @SerializedName("className")
    private String className;

    /**
     * 泛型类型Class名称
     */
    @SerializedName("genericTypes")
    private List<String> genericTypes;

    /**
     * POJO字段名称，或者参数字段名
     */
    @SerializedName("parameterName")
    private String parameterName;

    /**
     * 参数字段类型
     */
    @SerializedName("parameterType")
    private ParameterType parameterType;

    /**
     * 当type类型为POJO时，fields记录所有POJO成员字段的列表及其类型
     */
    @SerializedName("fields")
    private List<Parameter> fields;

    /**
     * 映射Http的参数名。表示值Value从网关Http请求的哪个数据字段中获取。
     */
    @SerializedName("httpName")
    private String httpName;

    /**
     * 指定从网关Http请求的数据源，Value值将从指定的数据源中读取。
     */
    @SerializedName("httpScope")
    private HttpScope httpScope;

    //

    public AnnotatedElement getElement() {
        return element;
    }

    public String getClassName() {
        return className;
    }

    public List<String> getGenericTypes() {
        return genericTypes;
    }

    public String getParameterName() {
        return parameterName;
    }

    public ParameterType getParameterType() {
        return parameterType;
    }

    public String getHttpName() {
        return httpName;
    }

    public HttpScope getHttpScope() {
        return httpScope;
    }

    public List<Parameter> getFields() {
        return fields;
    }

    public Parameter(AnnotatedElement element, String className, List<String> genericTypes, String parameterName, ParameterType parameterType, String httpName, HttpScope httpScope, List<Parameter> fields) {
        this.element = element;
        this.className = className;
        this.genericTypes = genericTypes;
        this.parameterName = parameterName;
        this.parameterType = parameterType;
        this.httpName = httpName;
        this.httpScope = httpScope;
        this.fields = fields;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private transient AnnotatedElement element;
        private String className;
        private List<String> genericTypes;
        private String fieldName;
        private ParameterType type;
        private String httpName;
        private HttpScope httpScope;
        private List<Parameter> fields;

        private Builder() {
        }

        public Builder element(AnnotatedElement element) {
            this.element = element;
            return this;
        }

        public Builder className(String className) {
            this.className = className;
            return this;
        }

        public Builder genericTypes(List<String> genericTypes) {
            this.genericTypes = genericTypes;
            return this;
        }

        public Builder fieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        public Builder type(ParameterType type) {
            this.type = type;
            return this;
        }

        public Builder httpName(String httpName) {
            this.httpName = httpName;
            return this;
        }

        public Builder httpScope(HttpScope httpScope) {
            this.httpScope = httpScope;
            return this;
        }

        public Builder fields(List<Parameter> fields) {
            this.fields = fields;
            return this;
        }

        public Parameter build() {
            return new Parameter(element, className, genericTypes, fieldName, type, httpName, httpScope, fields);
        }
    }

}
