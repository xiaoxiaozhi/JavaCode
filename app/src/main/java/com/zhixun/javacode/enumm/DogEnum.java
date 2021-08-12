package com.zhixun.javacode.enumm;

//枚举类型也可以由抽象方法 ，必须在实例中实现
public enum DogEnum {
    ONE {
        @Override
        public void say() {

        }
    };

    public abstract void say();
}
