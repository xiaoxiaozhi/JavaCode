package com.zhixun.javacode.multi_thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 */
public class ThreadPool {
    public static void main(String[] arg) {
        System.out.println("-----cachedThreadPool-----");
        // 使用完的线程会被重新利用，如果没有新的线程将会创建一个新的,如果线程驻留60S不被使用将被注销，
        cacheTest(Executors.newCachedThreadPool());
        System.out.println("-----fixedThreadPool-----");
        // 线程池内只有3个线程，谁先干完活谁接受下一个任务，线程编号一直是1、2 、3不再创建新的线程。等价于对第一种线程池做的限制
        fixedTest(Executors.newFixedThreadPool(3));

        System.out.println("-----ScheduledThreadPool-----");
        scheduledThreadPool();
        System.out.println("-----SingleThreadExecutor-----");
        // 建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序执行。
        singleTest(Executors.newSingleThreadExecutor());
//newSingleThreadExecutor 和 scheduledThreadPool 结合体
        Executors.newSingleThreadScheduledExecutor();

        submit();
    }

    /**
     * 线程池 执行 Runnable 和 Callable
     */
    public static void submit() {
//        Executors.newSingleThreadExecutor().su()
    }

    public static void cacheTest(ExecutorService executorService) {
        // 下面代码的意义 每隔 0~1000毫秒创建一个线程。
        for (int i = 0; i < 10; i++) {
            final int index = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(index + " 当前线程 " + Thread.currentThread().getName());
                }
            });
        }

    }

    public static void fixedTest(ExecutorService executorService) {
        // 下面代码意义是创建100个线程 每个线程的执行时间是5S
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("fixed " + Thread.currentThread().getName());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public static void scheduledThreadPool() {
        // 创建一个可以完全重复执行的线程池，可以代替task timer。corePoolSize 线程池大小，就算闲着也要创建这么多个。相当于
        // newFixedThreadPool 的加强版。这几个线程池层层递进
        // 延时执行 ：delay 延时X执行 X的单位
        Executors.newScheduledThreadPool(3).schedule(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("延时执行:" + Thread.currentThread().getName());
        }, 0, TimeUnit.SECONDS);

        // 延时并重复执行,从打印效果来看，一个线程池类，设置完重复执行再设置延时执行，使用的的是一个线程池。
        // 并且拥有重复利用线程的特性
        // 延时1秒执行:initialDelay 1 the time to delay first execution 只有第一次执行的时候有延迟，
        // 每隔3秒重复执行:period 3 the time to delay first execution
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(3);
        ses.scheduleAtFixedRate(() -> {
            System.out.println("固定间隔重复执行:" + Thread.currentThread().getName());
        }, 1, 3, TimeUnit.SECONDS);
        // 固定延迟 initialDelay 第一次执行的延迟 delay每次执行的间隔
        Executors.newScheduledThreadPool(3).scheduleWithFixedDelay(() -> {
            System.out.println("固定延迟重复执行:" + Thread.currentThread().getName());
        }, 1, 3, TimeUnit.SECONDS);
    }

    public static void singleTest(ExecutorService executorService) {
        for (int i = 0; i < 10; i++) {
            final int index = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("singleTest " + index + " " + Thread.currentThread().getName());
                }
            });
        }

    }


}
