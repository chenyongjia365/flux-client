package com.github.libchengo.flux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 陈哈哈 (yongjia-chen@outlook.com)
 */
@Configuration
public class FxConfiguration {

    @Bean
    FxDiscovery discovery(FxMetadataConfig metadataConfig) {
        return new FxDiscovery(metadataConfig);
    }

    @Bean("metadataConfig")
    FxMetadataConfig metadataConfig() {
        return new FxMetadataConfig();
    }
}
