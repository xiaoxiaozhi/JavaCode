package com.zhixun.javacode.enumm;

public class Excute {
    public static void main(String[] arg) {
//        PersonEnum
//        PersonEnum.ONE.name();
        System.out.println(PersonEnum.ONE.name() + "\t" + PersonEnum.ONE.ordinal());//枚举自带属性名字和序号
        System.out.println();
        System.out.println(PersonEnum.valueOf("ONE"));
//        PersonEnum.ONE.
    }

    //switch语句支持枚举
    static void choiceEnum(PersonEnum person) {
        switch (person) {
            case ONE:
                System.out.println(PersonEnum.ONE);
                break;
            case TWO:
                System.out.println(PersonEnum.TWO);
                break;
        }
    }
}
