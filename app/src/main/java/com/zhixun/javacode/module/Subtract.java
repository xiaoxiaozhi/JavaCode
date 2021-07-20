package com.zhixun.javacode.module;

public class Subtract {
	private Object lockObject;

	public Subtract(Object lock) {
		lockObject = lock;
	}

	public void subtrct() {
		synchronized (lockObject) {
			while (ValueObject.list.size() == 0) {
				System.out.println(Thread.currentThread().getName() + " 等待前");
				try {
					lockObject.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + " 等待后");
			}
			ValueObject.list.remove(0);
			System.out.println("ObjectValue size =" + ValueObject.list.size());
		}
	}
}
