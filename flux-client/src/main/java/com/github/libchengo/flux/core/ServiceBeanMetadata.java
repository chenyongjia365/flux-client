package com.github.libchengo.flux.core;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
public class ServiceBeanMetadata {

    private String application;
    private String prefix;
    private String version;
    private String group;
    private String interfaceName;
    private Class<?> interfaceClass;
    private List<Method> methods;

    public String getApplication() {
        return application;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getVersion() {
        return version;
    }

    public String getGroup() {
        return group;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String application;
        private String prefix;
        private String version;
        private String group;
        private String interfaceName;
        private Class<?> interfaceClass;
        private List<Method> methods;

        private Builder() {
        }

        public Builder application(String application) {
            this.application = application;
            return this;
        }

        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder group(String group) {
            this.group = group;
            return this;
        }

        public Builder interfaceName(String interfaceName) {
            this.interfaceName = interfaceName;
            return this;
        }

        public Builder interfaceClass(Class<?> interfaceClass) {
            this.interfaceClass = interfaceClass;
            return this;
        }

        public Builder methods(List<Method> methods) {
            this.methods = methods;
            return this;
        }

        public ServiceBeanMetadata build() {
            ServiceBeanMetadata serviceBeanMetadata = new ServiceBeanMetadata();
            serviceBeanMetadata.group = this.group;
            serviceBeanMetadata.prefix = this.prefix;
            serviceBeanMetadata.interfaceClass = this.interfaceClass;
            serviceBeanMetadata.methods = this.methods;
            serviceBeanMetadata.interfaceName = this.interfaceName;
            serviceBeanMetadata.application = this.application;
            serviceBeanMetadata.version = this.version;
            return serviceBeanMetadata;
        }
    }
}