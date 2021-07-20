package com.zhixun.javacode.module;

public class Main {
	synchronized public void method1() {
		System.out.println(Thread.currentThread().getName() + " method1ִ��ǰ-----");
		try {
			Thread.currentThread().sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " method1ִ�к�-----");
	}
}
