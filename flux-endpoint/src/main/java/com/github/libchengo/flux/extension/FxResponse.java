package com.github.libchengo.flux.extension;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Response结构
 *
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
public class FxResponse extends HashMap<String, Serializable> {

    public static final String NAME_FLUX_HTTP_STATUS = "@libchengo.flux.http.status";
    public static final String NAME_FLUX_HTTP_HEADERS = "@libchengo.flux.http.headers";
    public static final String NAME_FLUX_HTTP_BODY = "@libchengo.flux.http.body";

    private final HashMap<String, String> headers = new HashMap<>();

    private FxResponse() {
        super(3);
        field(NAME_FLUX_HTTP_HEADERS, headers);
        field(NAME_FLUX_HTTP_STATUS, 200);
    }

    /**
     * 设置StatusCode
     *
     * @param statusCode StatusCode
     * @return FxResponse
     */
    public FxResponse statusCode(int statusCode) {
        put(NAME_FLUX_HTTP_STATUS, statusCode);
        return this;
    }

    /**
     * 设置Body对象。
     *
     * @param body Body
     * @return FxResponse
     */
    public <T extends Serializable> FxResponse body(T body) {
        put(NAME_FLUX_HTTP_BODY, body);
        return this;
    }

    /**
     * 设置Headers
     *
     * @param headers Headers
     * @return FxResponse
     */
    public FxResponse headers(Map<String, String> headers) {
        this.headers.putAll(headers);
        return null;
    }

    /**
     * 设置Header对象。
     *
     * @param name  Header name
     * @param value Header value
     * @return FxResponse
     */
    public FxResponse header(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    /**
     * 设置Name-Value键值对到当前Map对象
     *
     * @param name  Name
     * @param value Value
     * @return FxResponse
     */
    public FxResponse field(String name, Serializable value) {
        put(name, value);
        return this;
    }

    /**
     * 获取指定Name的值，如果不存在，返回默认值
     *
     * @param name         Name
     * @param defaultValue 默认值
     * @param <T>          强制转换的类型
     * @return 值对象
     */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> T get(String name, T defaultValue) {
        return (T) this.getOrDefault(name, defaultValue);
    }

    /**
     * 从Map取走Name的值。Name对应的值会被移除。
     *
     * @param name Name
     * @param <T>  强制转换的类型
     * @return 值，或者为Null。
     */
    @SuppressWarnings("unchecked")
    public <T> T remove(String name) {
        return (T) super.remove(name);
    }

    //// Factory methods

    /**
     * 创建FxResponse对象
     *
     * @return FxResponse
     */
    public static FxResponse simple() {
        return new FxResponse();
    }

    /**
     * 创建Failed状态的FxResponse对象
     *
     * @return FxResponse
     */
    public static FxResponse ofFailed(String message) {
        return bodyMap().asFailed().message(message).build();
    }

    /**
     * 创建Success状态的FxResponse对象
     *
     * @return FxResponse
     */
    public static FxResponse ofSuccess(String message) {
        return bodyMap().asSuccess().message(message).build();
    }

    /**
     * 创建Error状态的FxResponse对象
     *
     * @return FxResponse
     */
    public static FxResponse ofError(String message) {
        return bodyMap().asError().message(message).build();
    }

    /**
     * 创建根据条件判定Success、Failed状态的FxResponse对象
     *
     * @return FxResponse
     */
    public static FxResponse ofCondition(boolean success, String message) {
        return bodyMap().statusOf(success).message(message).build();
    }

    /**
     * 创建一个Body为Map结构的FxResponse对象
     *
     * @return FxBodyMapResponse
     */
    public static BodyMapBuilder bodyMap() {
        return new BodyMapBuilder.SimpleBodyMapBuilder();
    }

}
