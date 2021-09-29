package com.zhixun.javacode.GenericParadigm;

/**
 * 泛型方法
 * 1.泛型方法可以定义在普通类中也可以定义在泛型类中
 * 2.泛型标识符放在修饰符后面，返回类型的前面
 */
public class ArrayAlg<E> {
    //1. 定义泛型方法  修饰符 <泛型列表> 返回类型 方法名
    public <E> E getMiddle1(E... arg) {
        return arg[arg.length / 2];
    }   //2. 泛型方法泛型独立于泛型类泛型（如果泛型类泛型标识是String，泛型方法可以使用Integer）

    //3. 泛型类的成员方法不能使静态方法
    public E getMiddle(E... arg) {
        return arg[arg.length / 2];
    }

    ///4. 泛型方法可以使用静态修饰
    public static <T, E> T getMiddle2(T... arg) {
        return arg[arg.length / 2];
    }


    //5. 类型限制 <T extends 类1 & 类2>
    public <T extends Comparable> T min(T... a) {
        if (a == null || a.length < 0) {
            return null;
        }
        T smaller = a[0];
        for (T t : a) {
            if (t.compareTo(smaller) < 0) {
                smaller = t;
            }
        }
        return smaller;
    }
}




