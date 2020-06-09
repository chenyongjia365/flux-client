package com.github.libchengo.flux.core;

import java.util.List;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 * @since 1.0.0
 */
public interface MetadataRegistry {

    String METADATA_NAMESPACE = "/flux-metadata";


    void startup();

    void shutdown();

    void submit(List<MethodMetadata> metadataList) throws Exception;

}
