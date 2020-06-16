package net.bytepower.flux.starter;

import net.bytepower.flux.core.MetadataDecoder;
import net.bytepower.flux.core.MetadataRegistry;
import net.bytepower.flux.core.MetadataResolver;
import net.bytepower.flux.impl.registry.ZookeeperMetadataRegistry;
import net.bytepower.flux.impl.registry.ZookeeperRegistryConfig;
import net.bytepower.flux.impl.resolver.MethodMetadataResolver;
import net.bytepower.flux.impl.support.JsonDecoder;
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
