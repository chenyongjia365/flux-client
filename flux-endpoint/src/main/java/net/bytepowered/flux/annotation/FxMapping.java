package net.bytepowered.flux.annotation;

import java.lang.annotation.*;

/**
 * 用于设置网关API映射
 *
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface FxMapping {

    /**
     * Http方法
     *
     * @return HttpMethod
     */
    FxMethod method();

    /**
     * 映射到网关的请求Uri路径
     *
     * @return Path
     */
    String path();

    /**
     * Dubbo的group参数。默认为空。
     *
     * @return Dubbo.Group
     */
    String group() default "";

    /**
     * Dubbo.Version参数。默认为空。
     *
     * @return Dubbo.Version
     */
    String version() default "";

    /**
     * 声明接口是否需要授权。默认为True。
     * 当设为False时，网关授权验证功能将忽略此接口
     *
     * @return True表示需要授权
     */
    boolean authorized() default true;
}
