package com.zhixun.javacode.module;

public class Stub extends Main {
	@Override
	public void method1() {
		System.out.println(Thread.currentThread().getName() + " method1执行前-----");
		try {
			Thread.currentThread().sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " method1执行后-----");
	}

}
