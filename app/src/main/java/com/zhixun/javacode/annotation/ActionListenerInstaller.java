package com.zhixun.javacode.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
                    if(!field.isAccessible()){
                        field.setAccessible(true);
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
