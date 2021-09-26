package com.zhixun.javacode.GenericParadigm;

/**
 * 泛型方法
 * 1.泛型方法可以定义在普通类中也可以定义在泛型类中
 * 2.泛型标识符放在修饰符后面，返回类型的前面
 */
public class ArrayAlg {
    public <T> T getMiddle(T... arg) {
        return arg[arg.length / 2];
    }
}
