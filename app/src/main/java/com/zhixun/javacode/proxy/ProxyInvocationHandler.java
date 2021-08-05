package com.zhixun.javacode.proxy;

import android.text.TextUtils;

import com.zhixun.javacode.annotation.ActionListenerFor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//Java 动态代理作用是什么？ - bravo1988的回答 - 知乎
//https://www.zhihu.com/question/20794107/answer/658139129
public class ProxyInvocationHandler<T> implements InvocationHandler {
    T obj;

    public ProxyInvocationHandler(T t) {
        obj = t;
    }

    public T getProxy() {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), obj.getClass().getInterfaces(), this);

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(obj, args);
//        ActionListenerFor actionListenerFor = method.getAnnotation(ActionListenerFor.class);
//        String source = actionListenerFor.source();
//        if (TextUtils.isEmpty(source)) {
//            return result;
//        } else {
//            return source + " 说：" + result;
//        }
        return method.getName() + " 说：" + result;
    }

}
