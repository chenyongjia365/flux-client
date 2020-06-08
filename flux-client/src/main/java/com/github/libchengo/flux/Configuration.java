package com.github.libchengo.flux;

import org.springframework.context.annotation.Bean;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
@Configuration
public class Configuration {

    @Bean
    Discovery discovery(EndpointConfig metadataConfig) {
        return new Discovery(metadataConfig);
    }

    @Bean("metadataConfig")
    EndpointConfig metadataConfig() {
        return new EndpointConfig();
    }
}
