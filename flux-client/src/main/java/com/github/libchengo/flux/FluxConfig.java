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
    private String basePackages;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String basePackages) {
        this.basePackages = basePackages;
    }

}
