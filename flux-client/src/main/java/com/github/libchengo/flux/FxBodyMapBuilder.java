package com.github.libchengo.flux;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈哈哈 (yongjia-chen@outlook.com)
 */
public interface FxBodyMapBuilder {

    /**
     * 设置StatusCode
     *
     * @param statusCode StatusCode
     * @return FxHttpResponse
     */
    FxBodyMapBuilder statusCode(int statusCode);

    /**
     * 设置Message字段
     *
     * @param message Message
     * @return FxHttpResponse
     */
    FxBodyMapBuilder message(String message);

    /**
     * 设置Status字段
     *
     * @param status Status
     * @return FxHttpResponse
     */
    FxBodyMapBuilder status(String status);

    /**
     * 设置Body的值，以data字段作为包装字段
     *
     * @param body Body值对象
     * @return FxHttpResponse
     */
    FxBodyMapBuilder body(Serializable body);

    /**
     * 设置Headers
     *
     * @param headers Headers
     * @return FxHttpResponse
     */
    FxBodyMapBuilder headers(Map<String, String> headers);

    /**
     * 设置Header Name-Value
     *
     * @param name  Header Name
     * @param value Header Value
     * @return FxHttpResponse
     */
    FxBodyMapBuilder header(String name, String value);

    /**
     * 创建Success状态。默认为200状态码
     *
     * @return FxHttpResponse
     */
    FxBodyMapBuilder asSuccess();

    /**
     * 设置为Failed状态。默认为400状态码
     *
     * @return FxHttpResponse
     */
    FxBodyMapBuilder asFailed();

    /**
     * 设置为Error状态。默认为500状态码
     *
     * @return FxHttpResponse
     */
    FxBodyMapBuilder asError();

    /**
     * 根据条件设置Success或Failed状态。状态码默认为200和400
     *
     * @param success 是否为Success状态
     * @return FxBodyMapResponse
     */
    FxBodyMapBuilder statusOf(boolean success);

    /**
     * 返回构建FxResponse对象
     *
     * @return FxResponse
     */
    FxResponse build();

    ////

    class SimpleBodyMapBuilder implements FxBodyMapBuilder {

        private final FxResponse response = FxResponse.simple();
        private final HashMap<String, Serializable> bodyMap = new HashMap<>(4);

        SimpleBodyMapBuilder() {
        }

        @Override
        public FxBodyMapBuilder statusCode(int statusCode) {
            response.statusCode(statusCode);
            return this;
        }

        @Override
        public FxBodyMapBuilder message(String message) {
            bodyMap.put("message", message);
            return this;
        }

        @Override
        public FxBodyMapBuilder status(String status) {
            bodyMap.put("status", status);
            return this;
        }

        @Override
        public FxBodyMapBuilder body(Serializable body) {
            bodyMap.put("data", body);
            return this;
        }

        @Override
        public FxBodyMapBuilder headers(Map<String, String> headers) {
            response.headers(headers);
            return this;
        }

        @Override
        public FxBodyMapBuilder header(String name, String value) {
            response.header(name, value);
            return this;
        }

        @Override
        public FxBodyMapBuilder asSuccess() {
            return statusCode(200).status("success").message("SUCCESS");
        }

        @Override
        public FxBodyMapBuilder asFailed() {
            return statusCode(400).status("failed").message("FAILED");
        }

        @Override
        public FxBodyMapBuilder asError() {
            return statusCode(500).status("error").message("ERROR");
        }

        @Override
        public FxBodyMapBuilder statusOf(boolean success) {
            if (success) {
                this.asSuccess();
            } else {
                this.asFailed();
            }
            return this;
        }

        @Override
        public FxResponse build() {
            response.body(bodyMap);
            return response;
        }

    }
}
