package com.zhixun.javacode.multi_thread;

/**
 * ThreadLocal ����һ�����ӣ�ÿ���̶߳��ܴ洢�Լ���˽������
 * @author zhixun
 *
 */
public class ThreadLocall {
	public static void main(String[] args) {
		ThreadLocal local = new ThreadLocal() {
			@Override
			protected Object initialValue() {
				return super.initialValue();
				// return "���get()������null �����︲д";
			}
		};
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					local.set("�߳�A" + i);
					System.out.println(local.get());
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					local.set("�߳�B" + i);
					System.out.println(local.get());
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();

	}
}
