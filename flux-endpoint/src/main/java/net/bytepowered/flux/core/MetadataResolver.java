package net.bytepowereded.flux.core;

import java.util.List;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 * @since 1.0.0
 */
public interface MetadataResolver {

    List<MethodMetadata> resolve(ServiceBeanMetadata beanMetadata);
}
