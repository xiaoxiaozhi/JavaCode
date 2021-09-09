package com.zhixun.javacode.other;

import java.util.Arrays;
import java.util.List;

public class T4 {
    public static void main(String[] arg) {
        new T1().read();
        int[] intArray = new int[]{1, 2, 3, 4, 5};
        int[] array = Arrays.copyOfRange(intArray, 1, 5);
        for (int i : array) {
            System.out.print(i);
        }
        List<String> strList = null;
        for (String str:strList) {
            System.out.print(str);
        }
    }

    static class Base {
        public void read() {
            if (this instanceof MyInterface) {
                System.out.print("MyInterface 子类");
            } else {
                System.out.print("不是MyInterface 子类");
            }
        }
    }

    static class T1 extends Base implements MyInterface {

    }

    interface MyInterface {

    }
}
