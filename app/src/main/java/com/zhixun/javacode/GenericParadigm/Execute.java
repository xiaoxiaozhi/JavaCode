package com.zhixun.javacode.GenericParadigm;

import android.util.Pair;

import com.zhixun.javacode.stream.Streamm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * https://www.bilibili.com/video/BV1xJ411n77R?p=4
 */
public class Execute {
    public static void main(String[] arg) {
        System.out.println("-----泛型方法-----");
        ArrayAlg<Integer> alg = new ArrayAlg<Integer>();
        //1.调用泛型方法
        alg.<String>getMiddle1("", "");//调用时在方法名前的尖括号确定泛型类型
        alg.getMiddle1("", "");//大多数情况不用放，编译器能够推断类型
//        System.out.println( double d = alg.getMiddle(1.1f, 2.5f, 1));
        //2.类型变量限定
//        new ArrayList<Fruit>().add(new Banana());//泛型类型不能用多态理解
        System.out.println(new ArrayAlg().min(1, 2, 3, 0));//min 内部代码调用了compare()怎么确定 T有compare() 方法？使用泛型限定<T extends Compare>
        //3.类型擦除
        //定义一个泛型类的同时会自动提供一个相应的原始类 就是删除泛型的类，擦除泛型标识，替换成第一个限定类型，无限定类型用Object代替
        //5. 泛型接口  原则和泛型派生类一致
        //6. 类型通配符? 代表具体的类型实参
        showPair(Arrays.asList("q", "p"));//showPair 只能接收字符串型的list，想要接收更多类型用 通配符 ?
        showPair1(Arrays.asList(1, 2));
        showPair2(Arrays.asList(1f, 2f));//类型通配符的上限 ? extends Number

    }

    class Fruit {
    }

    class Banana extends Fruit {
    }

    class Orange extends Fruit {
    }

    class Parent<T> {
        private T value;

        public T getValue() {
            return value;
        }
    }

    //4. 泛型派生类
    class Children1<T> extends Parent<T> {

    }//4.1 如果派生类是一个泛型类，要与父类泛型标识保持一致

    class Children2 extends Parent<String> {
        @Override
        public String getValue() {
            return super.getValue();
        }//4.2 如果派生类是一个普通类，父类必须要明确泛型类型
    }

    //
    static void showPair(List<String> list) {
        System.out.println(list);
    }

    //6.2 类型通配符 接收更多集合类型
    static void showPair1(List<?> list) {
        System.out.println(list);
    }

    //6.3 类型通配符 的上限
    static void showPair2(List<? extends Number> list) {
        System.out.println(list);
    }

}
