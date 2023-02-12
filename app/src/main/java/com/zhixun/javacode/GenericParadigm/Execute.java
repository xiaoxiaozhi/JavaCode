package com.zhixun.javacode.GenericParadigm;

import android.util.Pair;

import com.zhixun.javacode.stream.Streamm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 来自 java核心技术第一卷第八章
 * [类型擦除](https://www.bilibili.com/video/BV1ed4y1T7Ty/?spm_id_from=pageDriver&vd_source=9cc1c08c51cf20bda524430137dc77bb)
 * 1. 泛型类的定义
 * 类名<T1, T2, ..., Tn>,一个泛型类就是具有一个或多个类型变量(<T,V>)的类 ,查看类 Generic
 * 同一泛型类根据不同数据类型创建的对象本质上是同一个类型
 * attention:泛型不支持基本数据类型
 * 1.1 泛型类的继承
 * 代码在下面，如果派生类是一个泛型类，要与父类泛型标识保持一致，如果派生类是一个普通类，父类必须要明确泛型类型
 * 2. 泛型方法
 * 查看类 ArrayAlg
 * 3. 类型变量限定
 * 有时泛型类型需要实现特定的方法，例如 泛型类T调用 compareTo方法，解决这个问题的方案是将 T 限制为实现了 Comparable 接口
 * （只含一个方法 compareTo 的标准接口）的类。可以通过对类型变量 T 设置限定 实现这一点： public static <T extends Comparable> T min(T t)代码在ArrayAlg.min
 * <T extends Comparable> 输入的参数类型必须是 Comparable的子类
 * 一个类型变量或通配符可以有多个限定， 例如 <T extends Comparable & Serializable>
 * 4. 类型擦除
 * 虚拟机没有泛型类型对象，所有泛型类型都以Object显示，例如Parent<T>,泛型类型可以是Parent<String> 也可以是Parent<LocalDate>但在编译结束后都会变成原始类型Parent,原始类型用 Object替换T(代码在下面)
 * 如果有限定类型例如 Parent<T extends Comparable>,擦除后以Comparable代替(代码在下面);如果有两个限定类型<T extends 类1&类2>擦除后显示 第一个也就是类1
 * 5. 约束与局限性
 * 由于类型擦除的缘故，不管Parent<String>还是Parent<LocalDate> 编译后都是Parent(原始类型) 所以想用isInstance 关键字判断对象p是Parent<String>还是Parent<LocalDate>子类没有意义
 * 泛型不支持基本类型所有没有Pair<double>,只有 Pair<Double>,其原因是类型擦除。擦除之后，Pair类含有Object类型的域,而Object不能存储double值
 * 在泛型类中不能 new一个泛型标识符 例如 new T() 这样是不允许的
 * java不支持泛型类数组(代码在下面)  Parent<String>[] table = new Parent<String>[10] 这样做会失败 ，java数组支持协变但是极不推荐
 * java不支持泛型类协变 ArrayList<Fruit> 水果 = new ArrayList<Banana>() 这样做会报错，java不支持协变,当然也不支持逆变,
 * 用通配符?限定类型 <? extends 类名>以此让java支持协变。但是这样的协变只能读get不能写add. 可以add(null) 这是特例
 * 用通配符?限定类型<? super 类名>以此让java支持逆变。这样的逆变能写，但是get到的都是object类型，虽然逆变能让泛型类接收父类型的泛型类，但是添加(add)父类型仍然会报错 代码在下面
 * 逆变和协变用途，举个例子 java集合方法 Collections.copy(List<? super T> dest, List<? extends T> src)  src源头只读不写，dest目的只写不读.也就是说协变用在只读不写的地方，逆变用在只写不读的地方
 * 协变和逆变只针对泛型， 普通类是类型转换
 * 无限定通配符 <?> Parent<?> 相当于 Parent<T extends object>  kotlin 也有无限定通配符 *  List<*> 相当于 List<out Any>
 * 6. 泛型接口 原则和泛型派生类一致
 * <p>
 * attention:类型与泛型的关系变型(variant)有三种变型关系：协变(covariant)、逆变(contravariant)、不变(invariant)
 * 假设一个类型构造器f(type)，一个已知的类型被构造器 f(type)处理后得到一个崭新的类型，再假设两个类 Animal和Dog，后者是前者的子类
 * 协变就是 f(Animal) = f(Dog) 逆变 f(Dog)=f(Animal) 不变就是 f(Dog)和f(Animal)谁也不能接收谁
 * 类型构造器f 可以是泛型类 List<type> 数组f[] 还可以是函数 function(type),举个例子协变 Animal[] = Dog(),数组Animal接收Dog，逆变 Dog[] = Animal[]
 * <p>
 */
public class Execute {
    public static void main(String[] arg) {
        //1. 泛型类
        Generic g1 = new Generic<String, String>("", "");
        Generic g2 = new Generic("", 1);//也可以省略泛型标识符，自动推断类型
        System.out.println(g1.getClass() == g2.getClass());//同一泛型类根据不同数据类型创建的对象本质上是同一个类型

        System.out.println("-----泛型方法-----");
        ArrayAlg<Integer> alg = new ArrayAlg<Integer>();
        //2.泛型方法
        alg.<String>getMiddle1("", "");//调用时在方法名前的尖括号确定泛型类型
        alg.getMiddle1("", "");//大多数情况不用放，编译器能够推断类型
//        System.out.println( double d = alg.getMiddle(1.1f, 2.5f, 1));
        //3.类型变量限定
        System.out.println(new ArrayAlg().min(1, 2, 3, 0));//min 内部代码调用了compare()怎么确定 T有compare() 方法？使用泛型限定<T extends Compare>
        //4.类型擦除
        //5.约束与局限性
//        Parent<String>[] table = new Parent<String>[10];//java不支持泛型数组，如果可行的话下面这两行就会通过，显然这样做不符合逻辑
//        Object[] obj = table;
//        obj[0] = "123";//如果可行parent数组的元素就会被赋值String类型
        Fruit[] f = new Banana[10];//数组支持协变，这是因为java1.0还没有泛型，数组能通用处理,但是协变会让程序更容易出现异常极不推荐
//        f[0] = new Fruit();//报 .ArrayStoreException 异常 f本质是一个Banana数组，添加Fruit元素会报异常。
//        ArrayList<Fruit> 水果 = new ArrayList<Banana>();//java泛型类不支持协变和逆变
        ArrayList<? extends Fruit> sd = new ArrayList<Banana>(); //使用通配符限定类型<? extends 类名>让泛型类支持协变，但是这样的协变只能读get不能写add. 可以add(null) 这是特例
        ArrayList<? super Banana> sdd = new ArrayList<Fruit>();//使用通配符限定类型<? super 类名>让泛型类支持逆变
//        sdd.add(new Fruit());//虽然逆变能让泛型类接受 父类型的泛型类，但是添加父类型仍然会报错
        sdd.add(new Banana());//逆变只能添加指定泛型类以及子类 该例是Banana
        sdd.get(0);//编译器不知道这个父类具体是什么类，只能返回 Object 对象

        //6. 泛型接口  原则和泛型派生类一致
    }

    static void showBox(Box<Number> box) {
        Number first = box.getFirst();
        System.out.println("first-----" + first);
    }

    class Parent<T> {
        private T value;

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }

    //4.以泛型类class Parent<T>为例，虚拟机没有泛型概念，这就是类型擦除后的样子
    class Parent1 {
        private Object value;

        public Object getValue() {
            return value;
        }
    }

    //4. 如果是限定类型class Parent<T extends Comparable>,擦除后以Comparable代替
    class Parent2 {
        private Comparable value;

        public Comparable getValue() {
            return value;
        }
    }

    //1.1 泛型派生类
    class Children1<T> extends Parent<T> {

    }//1.1 如果派生类是一个泛型类，要与父类泛型标识保持一致

    class Children2 extends Parent<String> {
        @Override
        public String getValue() {
            return super.getValue();
        }//1.1 如果派生类是一个普通类，父类必须要明确泛型类型
    }

}
