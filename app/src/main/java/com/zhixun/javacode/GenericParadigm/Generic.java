package com.zhixun.javacode.GenericParadigm;

import java.util.ArrayList;
import java.util.List;

/**
 * 1. 泛型类
 * 一个泛型类就是具有一个或多个类型变量(<T,V>)的类
 */
public class Generic<T,U> {
    private T first;
    private U second;

    public Generic() {
        first = null;
        second = null;
    }

    public Generic(T first, U second) {
        this.first = first;
        this.second = second;
    }


    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    public void setFirst(T newValue) {
        first = newValue;
    }

    public void setSecond(U newValue) {
        second = newValue;
    }

}
