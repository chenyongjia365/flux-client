package com.github.libchengo.flux.core;

/**
 * 参数类型
 *
 * @author 陈哈哈 yongjia-chen@outlook.com
 */
public enum FxParameterType {

    /**
     * 基础字段类型。表示当前参数是Java基础类型，没有内部成员字段。
     */
    PARAMETER,

    /**
     * POJO类型。表示内部成员字段需要单独设值。
     */
    POJO_OBJECT

}
