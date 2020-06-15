package com.github.libchengo.flux.core;

import java.util.List;

/**
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 * @since 1.0.0
 */
public interface MetadataRegistry {

    String ROOT_NODE = "/flux";

    /**
     * Startup
     */
    void startup();

    /**
     * Shutdown
     */
    void shutdown();

    /**
     * Submit metadata
     *
     * @param metadataList Metadata
     * @throws Exception Error if
     */
    void submit(List<MethodMetadata> metadataList) throws Exception;

}
