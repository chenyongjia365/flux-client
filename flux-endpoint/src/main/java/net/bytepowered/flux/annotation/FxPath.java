package net.bytepowered.flux.annotation;

import java.lang.annotation.*;

/**
 * 表示从Http的动态路径Path参数中读取参数值。
 *
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Inherited
public @interface FxPath {

    /**
     * @see FxPath#name()
     */
    String value() default "";

    /**
     * 自定义映射Http请求的参数名；
     * 默认为空字符串，表示取Java接口的参数名称作为Http请求的参数名。
     *
     * @return 映射到Http请求的参数名。
     */
    String name() default "";

}
