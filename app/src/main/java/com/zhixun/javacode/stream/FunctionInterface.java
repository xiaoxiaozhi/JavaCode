package com.zhixun.javacode.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * java 四大核心函数式接口
 * Consumer<T>   消费类型接口： void accept(T t)  有输入没有输出
 * Supplier<T>   供给型接口：   T get()  无输入有输出
 * Function<T,R> 函数型接口：   R apply(T t) 映射
 * Predicate<T>  判断型接口：   boolean test(T t)
 */
public class FunctionInterface {
    public static void main(String[] arg) {
        happy(1000, (m) -> System.out.println("玩游戏消费" + m + "元"));
        System.out.println(getNumList(10, () -> (int) (Math.random() * 100)).stream().count());
        System.out.println(handleStr("niubi", (str) -> str.toUpperCase()));
        System.out.println(filterStr("niubi", (str) -> str.length() > 3).stream().count());
    }

    // 1. 消费型接口
    static void happy(int money, Consumer<Integer> con) {
        con.accept(money);
    }

    //2.供给型接口
    static List<Integer> getNumList(int num, Supplier<Integer> supplier) {
        List<Integer> lists = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            lists.add(supplier.get());
        }
        return lists;
    }

    //3.函数型接口
    static String handleStr(String str, Function<String, String> function) {
        return function.apply(str);
    }

    //4.Predicate 判断型接口,通常用于筛选
    static List<String> filterStr(String str, Predicate<String> predicate) {
        List<String> lists = new ArrayList<>();
        if (predicate.test(str)) {
            lists.add(str);
        }
        return lists;
    }
}
