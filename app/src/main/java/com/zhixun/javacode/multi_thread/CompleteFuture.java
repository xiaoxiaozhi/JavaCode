package com.zhixun.javacode.multi_thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * CompletableFuture.get() 与 join() 前者需要处理异常
 * runAsync：开启异步没有返回值
 * supplyAsync： 开启异步有返回值
 * thenCompose、thenCombine、thenApplyAsync、applyToEither、exceptionally
 */
public class CompleteFuture {
    public static void main(String[] args) {
        CompletableFuture.runAsync(() -> {
            System.out.println("开启异步没有返回值");
        }).join();
//        oneByOne();
//        twoByOne();
//        getFirst();
    }

    private static void oneByOne() {//依赖
        print("一起回家--->");
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {//supplyAsync 开启异步任务
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            print("做饭");
            return "";
        }).thenComposeAsync(dis -> CompletableFuture.supplyAsync(() -> {//这里用 thenApplyAsync 效果一样脏，
            print("吃饭");
            return "";
        }));//thenCompose:把前面异步任务的结果交给下一个异步任务，前一个任务完成下一个任务才会被触发，dis就是上一个supplier返回的参数
        print("老婆遛狗--->");
        cf.join();//返回执行结果,在这之前一直阻塞
    }

    private static void twoByOne() {
        print("一起回家--->");
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            print("炒菜");
            return "我炒菜--->";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {//thenCombine 把两个任务执行完成合并结果
            print("蒸饭");
            return "我蒸饭--->";
        }), (dish, rice) -> {
            return dish + rice + "一起吃饭";
        });//thenCompose:把前面异步任务的结果交给下一个异步任务，前一个任务完成下一个任务才会被触发，dis就是上一个supplier返回的参数
        print("老婆遛狗--->");
//        print("" + cf.join());//返回执行结果,在这之前一直阻塞
    }

    //两个任务同时运行，返回线运行完的线程结果
    private static void getFirst() {
        print("等公交665 和 556");
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            print("665正在赶来...");
            return "665路";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            print("556正在赶来...");
            return "556路";
        }), first -> first).exceptionally((e) -> {
            print(e.toString());
            return "公交车异常，打车";
        });
        print("坐" + future.join() + "回家");
    }

    private static void print(String tag) {
        System.out.println(Thread.currentThread().getId() + " " + tag);
    }

}
