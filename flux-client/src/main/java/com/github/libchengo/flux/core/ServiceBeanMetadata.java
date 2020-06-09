package com.github.libchengo.flux.core;

import java.util.HashMap;

public class ServiceBeanMetadata extends HashMap<String, Object> {

    public static final String META_PREFIX = "prefix";
    public static final String META_APP_NAME = "appName";
    public static final String META_GROUP = "group";
    public static final String META_VERSION = "version";
    public static final String META_IFACE_NAME = "interfaceName";
    public static final String META_IFACE_CLASS = "interfaceClass";
    public static final String META_FX_METHODS = "methods";

    public ServiceBeanMetadata(int initialCapacity) {
        super(initialCapacity);
    }

    public ServiceBeanMetadata set(String name, Object value) {
        put(name, value);
        return this;
    }

    public <T> T get(String name) {
        return getOrDefault(name, null);
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrDefault(String name, T defaultValue) {
        return (T) super.getOrDefault(name, defaultValue);
    }
}