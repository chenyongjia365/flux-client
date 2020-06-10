package com.github.libchengo.flux;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
@Component
@ConfigurationProperties(prefix = "flux")
public class FluxConfig {

    /**
     * 统一API前缀
     */
    private String prefix;

    /**
     * 扫描目录，多个路径以,分隔
     */
    private String basePackage;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

}
