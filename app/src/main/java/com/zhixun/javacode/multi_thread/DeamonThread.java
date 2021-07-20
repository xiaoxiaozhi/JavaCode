package com.zhixun.javacode.multi_thread;
/**
 * 守护线程，当程序中不再有普通线程时守护线程也会结束，例如垃圾回收线程
 *
 * @author zhixun
 *
 */
public class DeamonThread {
	public static void main(String... args) {
		Thread thread = new Thread() {
			private int i = 0;
			@Override
			public void run() {
				super.run();
				while (true) {
					System.out.println("i=" + i++);
					try {
						sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread.setDaemon(true);//设置deamon之后该线程编程守护线程，必须在start之前
		thread.start();
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
