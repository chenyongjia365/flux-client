package com.github.libchengo.flux.core;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Metadata 用于包装网关Http请求与后端Dubbo/Http服务请求的传输对象。
 *
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
public class MethodMetadata implements Serializable {

    private final String version = "v1";

    private String application;

    /**
     * 映射的协议名称
     */
    private SupportProtocol protocol;

    /**
     * Dubbo.group
     */
    private String rpcGroup;

    /**
     * Dubbo.version
     */
    private String rpcVersion;

    /**
     * Should authorize
     */
    private boolean authorize;

    /**
     * 网关连接目标服务路径，对应为后端Dubbo.interface或者Http地址
     */
    private String serviceUri;

    /**
     * 网关连接目标服务Method。对应为后端Dubbo.method或者Http.method
     */
    private String serviceMethod;

    /**
     * 网关侧定义的接收Http请求路径
     */
    private String httpUri;

    /**
     * 网关侧定义的接收Http请求Method
     */
    private String httpMethod;

    /**
     * 参数列表
     */
    private List<ParameterMetadata> parameters;

    public MethodMetadata(String application, SupportProtocol protocol, String rpcGroup, String rpcVersion, boolean authorize,
                          String serviceUri, String serviceMethod, String httpUri, String httpMethod,
                          List<ParameterMetadata> parameters) {
        this.application = application;
        this.protocol = protocol;
        this.rpcGroup = rpcGroup;
        this.rpcVersion = rpcVersion;
        this.authorize = authorize;
        this.serviceUri = serviceUri;
        this.serviceMethod = serviceMethod;
        this.httpUri = httpUri;
        this.httpMethod = httpMethod;
        this.parameters = parameters;
    }

    public String getApplication() {
        return application;
    }

    public String getVersion() {
        return version;
    }

    public SupportProtocol getProtocol() {
        return protocol;
    }

    public String getRpcGroup() {
        return rpcGroup;
    }

    public String getRpcVersion() {
        return rpcVersion;
    }

    public boolean isAuthorize() {
        return authorize;
    }

    public String getServiceUri() {
        return serviceUri;
    }

    public String getServiceMethod() {
        return serviceMethod;
    }

    public String getHttpUri() {
        return httpUri;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public List<ParameterMetadata> getParameters() {
        return parameters;
    }

    //

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String application;
        private SupportProtocol protocol;
        private String rpcGroup;
        private String rpcVersion;
        private boolean authorized;
        private String serviceUri;
        private String serviceMethod;
        private String httpUri;
        private String httpMethod;
        private List<ParameterMetadata> parameterMetadata;

        private Builder() {
        }

        public Builder application(String application) {
            this.application = application;
            return this;
        }

        public Builder protocol(SupportProtocol protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder rpcGroup(String rpcGroup) {
            this.rpcGroup = rpcGroup;
            return this;
        }

        public Builder rpcVersion(String rpcVersion) {
            this.rpcVersion = rpcVersion;
            return this;
        }

        public Builder authorized(boolean authorized) {
            this.authorized = authorized;
            return this;
        }

        public Builder serviceUri(String serviceUri) {
            this.serviceUri = serviceUri;
            return this;
        }

        public Builder serviceMethod(String serviceMethod) {
            this.serviceMethod = serviceMethod;
            return this;
        }

        public Builder httpUri(String httpUri) {
            this.httpUri = httpUri;
            return this;
        }

        public Builder httpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public Builder parameters(List<ParameterMetadata> parameterMetadata) {
            this.parameterMetadata = parameterMetadata;
            return this;
        }

        public MethodMetadata build() {
            return new MethodMetadata(application, protocol, rpcGroup, rpcVersion, authorized, serviceUri, serviceMethod, httpUri, httpMethod, parameterMetadata);
        }
    }
}
