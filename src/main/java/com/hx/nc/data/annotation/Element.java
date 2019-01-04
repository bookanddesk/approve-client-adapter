package com.hx.nc.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author XingJiajun
 * @Date 2019/1/3 10:46
 * @Description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Element {
    String name() default "";

    String nameRule() default "";

    ElementType type() default ElementType.Normal;

    ElementSource source() default ElementSource.Normal;

    enum ElementType {
        Normal, Complex, List
    }

    enum ElementSource {
        Normal, Parent, BaseFieldName
    }
}
