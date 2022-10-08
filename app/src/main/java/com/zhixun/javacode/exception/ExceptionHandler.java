package com.zhixun.javacode.exception;

/**
 * java核心技术第一卷第七章 异常处理
 * 1. 异常分类
 *    异常分为 Error 和 Exception， Throwable 是所有异常的基类
 *    1.1 Error 类层次结构描述了 Java 运行时系统的内部错误和资源耗尽错误。 应用程序不应该抛出这种类型的对象
 *    1.2 Exception有两个子类 RuntimeException 和 IOException。由程序错误导致的异常属于 RuntimeException ; 而程序本身没有问题， 但由于像 I/O 错误这类问题导致的异常属于IOException
 *        Java将派生于 Error 类 或 RuntimeException 类的所有异常称为非受查( unchecked ) 异常，派生于IOException异常称为受查（ checked) 异常
 */
public class ExceptionHandler {
    public static void main(String[] args) {

    }
}
