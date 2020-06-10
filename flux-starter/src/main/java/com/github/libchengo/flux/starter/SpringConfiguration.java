package com.github.libchengo.flux.starter;

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
        return new SpringBootstrap(clientConfig(), registry(), resolver());
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
        SpringRegistryConfig c = registryConfig();
        return new ZookeeperRegistryConfig(
                c.getSessionTimeoutMs(),
                c.getConnectionTimeoutMs(),
                c.getAddress());
    }

    @Bean
    SpringRegistryConfig registryConfig(){
        return new SpringRegistryConfig();
    }

    @Bean
    SpringClientConfig clientConfig() {
        return new SpringClientConfig();
    }
}
