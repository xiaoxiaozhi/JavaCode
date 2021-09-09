package com.zhixun.javacode.multi_thread.asynchronous;

import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

public class Execute {
    public static void main(String[] arg) {
        AsynCall asynCall = new AsynCall();
        new Thread(new FutureTask<Integer>(asynCall)).start();//FutureTask 可以把 Callable转化成 Future 和 Runnable
        try {
            System.out.println(asynCall.call());//call 方法被阻塞一直等到线程执行完成才会有返回值
            System.out.println(asynCall.call());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
