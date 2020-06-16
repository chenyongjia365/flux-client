package net.bytepower.flux.annotation;

/**
 * HTTP数据源范围
 *
 * @author 陈哈哈 chenyongjia365@outlook.com
 */
public enum FxScope {

    /**
     * 自动搜索Http参数
     */
    AUTO,

    /**
     * 获取Http Attrs单个数值
     */
    ATTR,

    /**
     * 获取Http Attrs整个Map结构
     */
    ATTRS,

    /**
     * 从From表单参数中获取
     */
    FORM,

    /**
     * 只从Header参数中读取
     */
    HEADER,

    /**
     * 只从Query和Form表单参数参数列表中读取
     */
    PARAM,

    /**
     * 从动态Path参数中获取
     */
    PATH,

    /**
     * 只从Query参数列表中读取
     */
    QUERY,
    ;
}
