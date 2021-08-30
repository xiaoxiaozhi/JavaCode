package com.zhixun.javacode.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 *
 */
public class ActionListenerInstaller {


    public static void processAnnotation(Object obj) {
        Class cl = obj.getClass();
        for (Method method : cl.getDeclaredMethods()) {
            ActionListenerFor annotation = method.getAnnotation(ActionListenerFor.class);
            if (annotation != null) {
                try {
                    Field field = cl.getDeclaredField(annotation.source());
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    System.out.println("反射字段的类型---" + field.getType().getSimpleName());
//                    field.getClass()

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void addListener(Object source, final Object param, final Method m) {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(method, args);
            }
        };
        Proxy.newProxyInstance(null, new Class[]{}, handler);
    }
}
