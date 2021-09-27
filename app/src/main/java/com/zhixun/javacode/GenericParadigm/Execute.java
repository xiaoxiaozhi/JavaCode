package com.zhixun.javacode.GenericParadigm;

public class Execute {
    public static void main(String[] arg) {
        System.out.println("-----泛型方法-----");
        ArrayAlg<Integer> alg = new ArrayAlg<Integer>();
        //1.调用泛型方法
        alg.<String>getMiddle1("", "");//调用时在方法名前的尖括号确定泛型类型
        alg.getMiddle1("", "");//大多数情况不用放，编译器能够推断类型
//        System.out.println( double d = alg.getMiddle(1.1f, 2.5f, 1));
    }
}
