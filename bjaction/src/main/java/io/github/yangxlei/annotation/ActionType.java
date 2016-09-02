package io.github.yangxlei.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yanglei on 16/8/29.
 *
 * Action 处理类的注解
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface ActionType {

    String value();
}
