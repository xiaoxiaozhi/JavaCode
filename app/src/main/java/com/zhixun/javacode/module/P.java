package com.zhixun.javacode.module;

public class P {
	private String lock;

	public P(String lock) {
		this.lock = lock;
	}

	public void setValue() {
		synchronized (lock) {
			if (!ValueObject.value.equals("")) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String value = System.currentTimeMillis() + "_" + System.nanoTime();
				System.out.println("set��ֵ" + value);
				ValueObject.value = value;
				lock.notify();
			}
		}
	}

}
