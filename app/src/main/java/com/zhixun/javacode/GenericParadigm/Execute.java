package com.zhixun.javacode.GenericParadigm;

public class Execute {
    public static void main(String[] arg) {
        System.out.println("-----泛型方法-----");
        ArrayAlg alg = new ArrayAlg();
        //1.调用泛型方法
        alg.<String>getMiddle("", "");//调用时在方法名前的尖括号放入具体类型
        alg.getMiddle("", "");//大多数情况不用放，编译器能够推断类型
//        System.out.println( double d = alg.getMiddle(1.1f, 2.5f, 1));
    }
}
