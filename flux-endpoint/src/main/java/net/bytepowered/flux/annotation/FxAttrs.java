package net.bytepowereded.flux.annotation;

import java.lang.annotation.*;

/**
 * 表示读取网关Http请求的Attr的全部内容，其数据结构为Map[String:Serializable]。
 *
 * @author 陈哈哈 (chenyongjia365@outlook.com)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Inherited
public @interface FxAttrs {
}
