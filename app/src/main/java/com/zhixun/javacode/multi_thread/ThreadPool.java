package com.zhixun.javacode.multi_thread;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * CompletableFuture https://zhuanlan.zhihu.com/p/63734729
 * new Thread的弊端如下：
 * a. 每次new Thread新建对象性能差。
 * b. 线程缺乏统一管理，可能无限制新建线程，相互之间竞争，及可能占用过多系统资源导致死机或oom,缺乏更多功能，如定时执行、定期执行、线程中断。
 * 相比new Thread，Java提供的四种线程池的好处在于：
 * a. 重用存在的线程，减少对象创建、消亡的开销，性能佳。
 * b. 可有效控制最大并发线程数，提高系统资源的使用率，同时避免过多资源竞争，避免堵塞。
 * c. 提供定时执行、定期执行、单线程、并发数控制等功能。
 * 自定义线程池ThreadPool，以上四种线程池都是java对他的定制
 */
public class ThreadPool {
    public static void main(String[] arg) {
        System.out.println("-----cachedThreadPool-----");
        // 使用完的线程会被重新利用，如果没有新的线程将会创建一个新的,如果线程驻留60S不被使用将被注销，
        cacheTest(Executors.newCachedThreadPool());
        System.out.println("-----fixedThreadPool-----");
        // 线程池内只有3个线程，谁先干完活谁接受下一个任务，线程编号一直是1、2 、3不再创建新的线程JobService。等价于对第一种线程池做的限制
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

    /**
     * Callable 和 Runable的区别
     * executorService.submit执行Callable和Runable的时候。Callable<>利用泛型返回特定类型
     */
    public static void submit() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        // executorService.submit 执行的不管事callable还是Runable 。run或者call方法总是立即执行，
        // 但是future.get 比较奇怪，所执行执行时间等于 run或者call的时间，同时这两个方法不会再执行一次。future.get到底做了什么呢
        // executorService.submit 和 executorService.execute区别 前者配合Callable获取返回值且只能有这一种配合。
        long beginTime = System.currentTimeMillis();
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                // executorService.submit zhihou
                Thread.sleep(1000);
                System.out.println("get" + Thread.currentThread().getName());
                return "get" + Thread.currentThread().getName();
            }
        });
//        // Runnable 和callable的代码都在子线程中执行，future.get在主线程中个执行
//        // 执行线程
        Future<String> s = executorService.submit(new Runnable() {
            @Override
            public void run() {
                long endTime = System.currentTimeMillis();
                try {
                    System.out.println("执行一编");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("子线程耗时" + (endTime - beginTime) + " Millisecond!");
            }
        }, "这是给future的");
        try {
            String ss = s.get();
            // 打印结果
            long endTime = System.currentTimeMillis();
            System.out.println("是主线程吗" + Thread.currentThread().getName());
            System.out.println(ss + (endTime - beginTime) + " Millisecond!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//
//        List<Future<String>> resultList = new ArrayList<Future<String>>();
//
//        // 创建10个任务并执行
//        for (int i = 0; i < 10; i++) {
//            // 使用ExecutorService执行Callable类型的任务，并将结果保存在future变量中
//            Future<String> future1 = executorService.submit(new TaskWithResult(i));
//            // 将任务执行结果存储到List中
//            resultList.add(future1);
//        }

        // 遍历任务的结果
        // for (Future<String> fs : resultList) {
        // try {
        // System.out.println(fs.get()); // 打印各个线程（任务）执行的结果
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // } catch (ExecutionException e) {
        // //发生异常fs.get() 这个循环理发停止执行，如果上面10个循环线程还没有执行完，未执行的线程也全部停止
        // executorService.shutdownNow();
        // e.printStackTrace();
        // break;
        // }
        // }
        // MyAsyncTask myAsyncTask = new MyAsyncTask();//执行报错java项目里不可用Android类库
        // myAsyncTask.execute("");//执行
    }

    public static void schedulTest(ScheduledExecutorService service) {
        // 下面代码的意义 每隔 0~1000毫秒创建一个线程。
        for (int i = 0; i < 10; i++) {
            final int index = i;
            try {
                // System.out.println(Thread.currentThread().getName());
                Thread.sleep(index * 100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // service.schedule()
        }
    }

    static class TaskWithResult implements Callable<String> {
        private int id;

        public TaskWithResult(int id) {
            this.id = id;
        }

        /**
         * 任务的具体过程，一旦任务传给ExecutorService的submit方法，则该方法自动在一个线程上执行。
         *
         * @return
         * @throws Exception
         */
        public String call() throws Exception {
            System.out.println("call()方法被自动调用,干活！！！             " + Thread.currentThread().getName());
            if (new Random().nextBoolean())
                throw new ThreadOperation.TaskException("Meet error in task." + Thread.currentThread().getName());
            // 一个模拟耗时的操作
            // for (int i = 999999999; i > 0; i--)
            ;
            return "call()方法被自动调用，任务的结果是：" + id + "    " + Thread.currentThread().getName();
        }
    }
}
