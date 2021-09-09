package com.zhixun.javacode.multi_thread.asynchronous;

import java.util.concurrent.Callable;

/**
 * 异步
 * Runnable 可以看做是个 没有参数与返回值的异步方法
 * Callable 没有参数有返回值的异步方法。
 * Feature 保存异步计算的结果，计算好之后就能获取，否则一直被阻塞
 */
public class AsynCall implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        Thread.sleep(5 * 1000);
        return 5;
    }
}
//public interface Future<V>
//{
//    V get() throws . .
//    V get(long timeout, TimeUnit unit) throws . .
//    void cancel (boolean maylnterrupt);
//    boolean isCancelledO;
//    boolean isDoneO;
//}
