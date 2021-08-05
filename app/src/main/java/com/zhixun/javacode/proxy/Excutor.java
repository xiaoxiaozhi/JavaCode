package com.zhixun.javacode.proxy;

public class Excutor {
    public static void main(String[] arg) {
        People worker = new Worker();
        ProxyInvocationHandler<People> proxyInvocationHandler = new ProxyInvocationHandler<>(worker);
        System.out.println(proxyInvocationHandler.getProxy().introduce());
    }
}
