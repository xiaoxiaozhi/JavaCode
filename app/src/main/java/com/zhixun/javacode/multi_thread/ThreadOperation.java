package com.zhixun.javacode.multi_thread;

import com.zhixun.javacode.module.FileInfo;
import com.zhixun.javacode.module.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;


/**
 * 1. 线程基本介绍
 * 2. 中断
 * 停止一个线程 :stop() suspend() resume()等都是过期作废的方法。正确做法是interrupt()给线程打一个标记，然后线程时不时的检查标志 Thread.currentThread().isInterrupted()
 * 如果检查到为true，如何处理要根据自己的需求。建议 手动抛出 throw new InterruptedException() 在catch中处理异常
 * 当一个被阻塞的线程（调用 sleep 或 wait) 上调用 interrupt 方法时，阻塞调用将会被Interrupted Exception 异常中断
 * <p>
 * 并行与并发：
 * 并行：多个cpu实例或者多台机器同时执行一段处理逻辑，是真正的同时。
 * 并发：通过cpu调度算法，让用户看上去同时执行，实际上从cpu操作层面不是真正的同时。并发往往在场景中有公用的资源，那么针对这个公用的资源往往产生瓶颈，我们会用TPS或者QPS来反应这个系统的处理能力。
 * 设置JVM参数-server 鼠标放在该类，右键run as ---》 runConfigrations--->arguments---> -server  试验volatile关键字
 */
public class ThreadOperation {
    public static void main(String... args) {
        //2. 中断
        myInterrupted();


//        // https://mp.weixin.qq.com/s?__biz=MzIwMTY0NDU3Nw==&mid=2651936591&idx=1&sn=3309d187423b08ce2591d78234547370&chksm
//        // =8d0f3801ba78b117434aaa1571a1088dbafe0c72443103a288221edeb72cd602174109f26281&mpshare=1&scene=24&srcid=0507Dj7JoFepGvaZbTwSvVOt&key=0040280a5d195d7fb1c2064d0d57fbaa9115c31be1d897093a5ffbf6b55a93ab72390f6b117e0740af1ea9132caefe463524a2cc47e060b1c60e6d2d09c74cdc5f36603f18eb38ecf4adddc0179a1e30&ascene=14&uin=OTU5NTc0NDYw&devicetype=Windows+10&version=62060728&lang=zh_CN&pass_ticket=ZNf%2BsjTIPUpIBbG%2FX4z8lndNnOBNELNQbFd9MiHyqs5NqZx22hbnF9rdV1I1n9UU
//        // 实现线程的两个方法，1. 继承Thread类实现run方法，本例以匿名内部类举例 2. 实现Runnable接口，
//        Thread thread1 = new Thread() {
//            private int ticks = 5;
//            String threadName = this.getName();// run方法之外获取该线程名字
//
//            public void ss() {
//                // this.isAlive();//run方法之外获取线程
//                // Thread.currentThread().isAlive();//结果为主线程是否存活
//            }
//
//            @Override
//            public void run() {
//                super.run();
//                System.out.printf("继承Thread类实现线程");
//                for (int i = 0; i < 100; i++) {
//                    if (ticks > 0) {
//                        System.out.printf("ticks:----" + ticks--);
//                    }
//                }
//            }
//        };
//        // new Thread1(); 会发现每个线程各卖5张票
//        // Thread 类也实现了Runable接口，不过Thread的run方法实际执行的是传入的Runnable的run方法，这实际上是一种代理模
//        // 李兴华那本书讲到这里说Runnable更适合多线程共享资源，实际测试后发现是错误的，仍然是非线程安全。
//        List<Integer> list = new ArrayList<>();
//        Runnable r = new Runnable() {
//            int ticks = 50;
//
//            @Override
//            public void run() {
//                // for (int i = 0; i < 1000; i++)
//                // {//这是李兴华的例子。由于一个线程卖的太多票，所以不容易出现安全问题。当去掉for循环一个线程只卖一张票。安全问题就会明显产生
//                if (ticks > 0) {
//                    // ticks
//                    // list.add(ticks);
//                    System.out.println(Thread.currentThread().getName() + "售票：" + ticks--);// 经测试有一张票被两个线程同时卖掉
//                }
//                // }
//            }
//        };
//        // 非线程安全：多个线程操作同一个变量，值不同步的情况
//        // 加 synchronized ()的代码块称为同步区或者互斥区，当线程想要执行互斥区的代码，线程就会尝试去拿同步锁，拿到就会执行拿不到就会不断去尝试。
//        // isAlive() 方法判断线程是否存活，从调用start()方法后到run方法执行这个阶段即时被阻塞也一直都是存活状态，
//        for (int i = 0; i < 100; i++) {
//            new Thread(r).start();
//        }
//
//        // yield()放弃当前cpu资源，放弃时间不确定有可能马上执行
//        Thread thead5 = new Thread() {
//            public void run() {
//                super.run();
//                long curr = System.currentTimeMillis();
//                for (int i = 0; i < 50000; i++) {
//                    // yield();
//                }
//                System.out.println("线程5耗时" + (System.currentTimeMillis() - curr) + "ms");
//            }
//
//            ;
//        };
//        thead5.start();
//        // thead5.yield();//在主线程执行yield大部分情况下 thread5都已经执行完毕
//
//        // 线程默认优先级5.。主线程提高优先级后在主线程上开启的线程优先级也提高，这属于线程优先级的继承性，从线程A启动线程B，线程B的优先级会和A一致。
//        // 线程的优先级范围1~10 通过setPriority(int newPriority)设置优先级 数值越大优先级越高。要在start()前执行
//        // 高优先级的线程大概率先执行完，但不代表一定先执行完，当优先级差异很大时谁先执行完就会和调用先后顺序无关和优先级有关，总的来说优先级越大先执行完的概率也就越大，如果相差比较小则效果不太明显
//        Thread thead6 = new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                System.out.println("线程6优先级：" + this.getPriority());
//            }
//        };
//        thead6.start();
//
//        System.out.println("主线程优先级a：" + Thread.currentThread().getPriority());
//        Thread.currentThread().setPriority(6);
//        System.out.println("线程6优先级：" + thead6.getPriority());
//        System.out.println("主线程优先级b：" + Thread.currentThread().getPriority());
//        Thread thread7 = new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                System.out.println("线程7优先级：" + this.getPriority());// 优先级随着主线程提高
//            }
//        };
//        thread7.start();
//        // 守护线程 ：thread.setDeamon,例如垃圾回收线程，当程序中不存在普通线程了，守护线程就会自动销毁 示例DeamonThread.java
//
//        // 示例synchornizedThread.java。
//        // 加同步锁的静态方法，和普通方法里面有同步代码块，锁定的对象不一样前者是类，对所有实例都起作用；后者是实例。例如Test 静态同步锁 方法1
//        // 静态同步锁方法2 同步锁方法3
//        // 创建实例。三个线程分别执行方法1 方法2 方法3. 方法1和方法2 同步，方法3和其它两个方法异步。这是因为静态同步锁方法的锁对象是class类而不是实例
//        // 小结：分清同步锁，锁的是实例还是class类，
//        // synchronized(Test.claa) 的效果和 synchronize static 方法 。的效果一样
//        new Service();
//
//        // 死锁检查工具 java多线程编程核心 p123页
//
//        // new Thread(new Thread());// Thread 构造函数 传Runnable的子类，Thread类也继承了Runnable接口
//        // 线程的5个状态：创建 new Thread()创建线程。
//        // 就绪 调用start()方法后线程就进入就绪状态排队等待分配cpu执行；或者从堵塞（等待、睡眠）状态返回
//        // 运行 获得cup资源后线程自动进入运行状态 run()方法包含了线程所有操作。
//        // 堵塞、 某些特殊原因线程被挂起 sleep() suspend() wait() 线程进入阻塞状态，原因解除前都不能再进入排队队列
//        // 死亡 线程调用stop()或者run()方法后线程进入死亡状态 每个线程只能start()一次不能不能重复执行
//        // join()方法 ：线程强制运行，在此期间其它线程不能运行
////        Thread thread2 = new Thread(() -> {
////            for (int j = 0; j < 5; j++) {
////                System.out.printf(Thread.currentThread().getName() + "-----" + j);
////            }
////        }, "线程");
////        thread2.start();
////        System.out.println("");
////        for (int i = 0; i < 20; i++) {
////            if (i > 10) {
////                try {
////                    thread.join();// 通过打印结果可以看到主线程，执行到i=10的时候开始执行线程，线程执行完后又开始执行主线程
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////            }
////            System.out.printf("主线程-----" + i);
////        }
//
//        // Thread.yield()方法作用是：暂停当前正在执行的线程对象，并执行其他优先级相同线程。yield让线程从运行状态转移到就绪状态，而不是阻塞。实际中肯能没有效果
//
//
//        threadOptimization();
//        CountDownLatch cd = new CountDownLatch(100);
    }

    private static void myInterrupted() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
//                    sleep(20000);//阻塞线程调用interrupt()直接抛出 InterruptedException
                    for (int i = 0; i < 100; i++) {
                        System.out.printf("i=%d\t", i);
                        if (this.isInterrupted()) {
                            System.out.println("isInterrupted == true 产生中断");
                            // return;//虽然能终止线程但是在catch中处理能做更多的操作
                            throw new InterruptedException();
                        }
                    }
                    System.out.printf("在for下面---");
                } catch (InterruptedException e) {
                    System.out.println("异常中断程序----");
                    e.printStackTrace();
                    return;
                }
                System.out.println("异常终止线程---");
            }
        };

        try {
            thread.start();
            Thread.sleep(10);
            thread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 暂停线程 suspend() 暂停 resume() 恢复.suspend缺点很明显，1.会占用公共资源阻止其它线程复用2.不同步
        // 1.组织其他线程复用：线程A和线程B执行 一个synchronized同步
        // 方法。A线程执行过程中被suspend挂起。A线程暂停执行，同步锁得不到释放，b线程无法再执行同步方法
        // 把B线程替换为主线程，主线程由于得不到锁，程序将锁死
        // 2.不同步：线程a修改值1然后suspend值2 没修改。线程b取值1和值2.打印出来的数字值1修改值2 未修改。值1和值2不加同步锁
        try {
            MyThread4 thread4 = new MyThread4();
            thread4.start();
            Thread.sleep(500);
            thread4.suspend();
            System.out.println("A=" + System.currentTimeMillis() + "  i=" + thread4.getI());
            Thread.sleep(500);
            System.out.println("A=" + System.currentTimeMillis() + "  i=" + thread4.getI());
            thread4.resume();
            Thread.sleep(500);
            thread4.suspend();
            System.out.println("B=" + System.currentTimeMillis() + "  i=" + thread4.getI());
            thread.sleep(500);
            System.out.println("B=" + System.currentTimeMillis() + "  i=" + thread4.getI());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static class CallableImpl implements Callable<String> {

        public CallableImpl(String acceptStr) {
            this.acceptStr = acceptStr;
        }

        private String acceptStr;

        @Override
        public String call() throws Exception {
            // 任务阻塞 1 秒
            Thread.sleep(1000);
            System.out.println("Callable:" + Thread.currentThread().getName());
            return this.acceptStr + " append some chars and return it!";
        }

    }


    static class TaskException extends Exception {
        public TaskException(String message) {
            super(message);
        }
    }

    static class MyThread4 extends Thread {
        private long i = 0;

        public long getI() {
            return i;
        }

        public void setI(long i) {
            this.i = i;
        }

        public void run() {
            super.run();
            while (true) {
                i += 1;
                // System.out.println("在线程4中打印i="+i); //println()是一个同步方法
                // synchronized。当线程被suspend。同步锁得不到释放，其它地方再也不能用println打印结果了
            }
        }
    }

    static void threadOptimization() {
        List<FileInfo> fileList = new ArrayList<FileInfo>();
        for (int i = 0; i < 30000; i++) {
            fileList.add(new FileInfo("身份证正面照", "jpg", "101522", "md5" + i, "1"));
        }
        long startTime = System.currentTimeMillis();

        for (FileInfo fi : fileList) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();

        System.out.println("单线程耗时：" + (endTime - startTime) + "ms");
    }
    // 发现Runable接口的run方法加了abstract。其实接口里面的方法都是必须由实现类去实现的，所有接口中的方法都是public abstract，
    // 只是平时习惯上abstract是不写的，public关键字也可以不写，所以写上abstract没有意义
    // 其实都无所谓，最后生成class文件后，都会自动加上public abstract。如源码：
    // public interface AAA {
    // void aaa();
    // }
    //
    //
    // 反编译后：
    // public abstract interface AAA
    // {
    // public abstract void aaa();
    // }
}