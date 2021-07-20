package com.zhixun.javacode.module;

public class Add {
	private Object lockObject;

	public Add(Object lock) {
		lockObject = lock;
	}

	public void add() {
		synchronized (lockObject) {
			ValueObject.list.add("加");
			lockObject.notifyAll();
		}
	}
}
