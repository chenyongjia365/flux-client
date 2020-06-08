package com.github.libchengo.flux.core;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * FxDefinition 用于包装网关Http请求与后端Dubbo/Http服务请求的传输对象。
 *
 * @author 陈哈哈 (yongjia-chen@outlook.com)
 */
public class FxDefinition implements Serializable {

    @SerializedName("ver")
    private final String ver = "v1";

    @SerializedName("application")
    private String application;

    /**
     * 映射的协议名称
     */
    @SerializedName("protocol")
    private FxProtocol protocol;

    /**
     * Dubbo.group
     */
    @SerializedName("rpcGroup")
    private String rpcGroup;

    /**
     * Dubbo.version
     */
    @SerializedName("rpcVersion")
    private String rpcVersion;

    /**
     * Should authorize
     */
    @SerializedName("authorized")
    private boolean authorized;

    /**
     * 网关连接目标服务路径，对应为后端Dubbo.interface或者Http地址
     */
    @SerializedName("serviceUri")
    private String serviceUri;

    /**
     * 网关连接目标服务Method。对应为后端Dubbo.method或者Http.method
     */
    @SerializedName("serviceMethod")
    private String serviceMethod;

    /**
     * 网关侧定义的接收Http请求路径
     */
    @SerializedName("httpUri")
    private String httpUri;

    /**
     * 网关侧定义的接收Http请求Method
     */
    @SerializedName("httpMethod")
    private String httpMethod;

    /**
     * 参数列表
     */
    @SerializedName("parameters")
    private List<FxParameter> parameters;

    public FxDefinition(String application, FxProtocol protocol, String rpcGroup, String rpcVersion, boolean authorized,
                        String serviceUri, String serviceMethod, String httpUri, String httpMethod,
                        List<FxParameter> parameters) {
        this.application = application;
        this.protocol = protocol;
        this.rpcGroup = rpcGroup;
        this.rpcVersion = rpcVersion;
        this.authorized = authorized;
        this.serviceUri = serviceUri;
        this.serviceMethod = serviceMethod;
        this.httpUri = httpUri;
        this.httpMethod = httpMethod;
        this.parameters = parameters;
    }

    public String getApplication() {
        return application;
    }

    public String getVer() {
        return ver;
    }

    public FxProtocol getProtocol() {
        return protocol;
    }

    public String getRpcGroup() {
        return rpcGroup;
    }

    public String getRpcVersion() {
        return rpcVersion;
    }

    public boolean isAuthorized() {
        return authorized;
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

    public List<FxParameter> getParameters() {
        return parameters;
    }

    //

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String application;
        private FxProtocol protocol;
        private String rpcGroup;
        private String rpcVersion;
        private boolean authorized;
        private String serviceUri;
        private String serviceMethod;
        private String httpUri;
        private String httpMethod;
        private List<FxParameter> parameters;

        private Builder() {
        }

        public Builder application(String application) {
            this.application = application;
            return this;
        }

        public Builder protocol(FxProtocol protocol) {
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

        public Builder parameters(List<FxParameter> parameters) {
            this.parameters = parameters;
            return this;
        }

        public FxDefinition build() {
            return new FxDefinition(application, protocol, rpcGroup, rpcVersion, authorized, serviceUri, serviceMethod, httpUri, httpMethod, parameters);
        }
    }
}
