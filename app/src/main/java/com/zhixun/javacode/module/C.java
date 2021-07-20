package com.zhixun.javacode.module;

public class C {
	private String lock;

	public C(String lock) {
		this.lock = lock;
	}

	public void getValue() {
		synchronized (lock) {
			if (ValueObject.value.equals("")) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String value = System.currentTimeMillis() + "_" + System.nanoTime();
				System.out.println("get的值ֵ" + value);
				ValueObject.value = "";
				lock.notify();
			}
		}
	}
}
