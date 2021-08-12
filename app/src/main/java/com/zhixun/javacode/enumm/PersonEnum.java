package com.zhixun.javacode.enumm;

//视频 https://www.bilibili.com/video/BV1xE41127mh?p=2
//相当于一个不能继承的final 类
//主要用在单例模式，通过class类创建单例模式实际上是失败的，因为通过反射可以创建很多，
public enum PersonEnum {
    ONE, TWO(100);//写在最前面，其它地方会报错。 线程安全，无法通过反射创建实例
    //这个类的实例

    //像class类一样拥有 字段和方法
    private PersonEnum() {
        //枚举类型 其实也有构造方法，只是private修饰外界调用不了
    }

    private PersonEnum(int s) {
        price = s;//枚举类型构造方法
    }

    public int price;

    private void print() {
        System.out.println("123456789");
    }
}
