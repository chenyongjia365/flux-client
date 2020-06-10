package com.github.libchengo.flux;

import com.github.libchengo.flux.core.MetadataDecoder;
import com.github.libchengo.flux.core.MetadataRegistry;
import com.github.libchengo.flux.core.MetadataResolver;
import com.github.libchengo.flux.impl.registry.ZookeeperMetadataRegistry;
import com.github.libchengo.flux.impl.registry.ZookeeperRegistryConfig;
import com.github.libchengo.flux.impl.resolver.MethodMetadataResolver;
import com.github.libchengo.flux.impl.support.JsonDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
@Configuration
public class SpringConfiguration {

    @Bean
    SpringBootstrap bootstrap() {
        return new SpringBootstrap(fluxConfig(), registry(), resolver());
    }

    @Bean
    MetadataDecoder decoder() {
        return new JsonDecoder();
    }

    @Bean
    MetadataResolver resolver() {
        return new MethodMetadataResolver();
    }

    @Bean
    MetadataRegistry registry() {
        return new ZookeeperMetadataRegistry(zookeeperConfig(), decoder());
    }

    @Bean
    ZookeeperRegistryConfig zookeeperConfig() {
        return new ZookeeperRegistryConfig();
    }

    @Bean
    FluxConfig fluxConfig() {
        return new FluxConfig();
    }
}
