package com.zhixun.javacode.lambdaa;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

//1.lambda表达式 (参数) -> 表达式 ，如果表达式不止一行 必须用{}  注意如果没有参数的时候必须使用空括号()->{表达式}
public class Lambda {
    public static void main(String[] arg) {
        String[] planets = new String[]{"Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};
        Arrays.sort(planets, (str1, str2) -> str1.charAt(0) - str2.charAt(0));//无需指定lambda的返回，由上下文推定
        //Comparator 大于0排前面
        System.out.println(Arrays.asList(planets));
        new Timer();
        //3.方法引用
        Arrays.sort(planets, String::compareTo);
        testInterface(() -> "123");

    }

    public static void testInterface(MyInterface myInterface) {
        System.out.println(myInterface.getName());

    }

    class MyComparable implements MyInterface {


        @Override
        public String getName() {
            return MyComparable.class.getName();
        }

    }

    /**
     * 2.函数式接口：①只有一个抽象方法的接口。
     * ②default方法某默认实现，不属于抽象方法
     * ③接口重新声明了Object的公共方法 例如Comparator内的boolean equals(Object obj);
     * Java SE 8 中，允许在接口中增加静态方法, 只是这有违于将接口作为抽象规范的初衷
     * 通常的做法都是将静态方法放在伴随类中。在标准库中， 你会看到成对出现
     * 的接口和实用工具类， 如 Collection/Collections 或 Path/Paths
     */
    interface MyInterface {
        //默认方法
        default int getData() {
            getString();
            return 1;
        }

        /**
         * 1.静态方法可以有多个
         * 2.静态方法不能被继承及覆盖，所以只被具体所在的接口调用
         * 3.静态方法通过接口名调用
         */
        static String getString() {
            return MyInterface.class.getName();
        }

        String getName();
    }
}
