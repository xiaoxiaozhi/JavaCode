package com.zhixun.javacode.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在java中注解被当做一个修饰符来使用（修饰符例如 public private）
 * 处理注解的工具(ActionListenerInstaller)将接收实现了@Test的那些方法
 */
@Target(ElementType.METHOD)//元注解
@Retention(RetentionPolicy.RUNTIME)//元注解
public @interface ActionListenerFor {
    String source();

}
