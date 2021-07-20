package com.zhixun.javacode.multi_thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ���Թ�ƽ���ͷǹ�ƽ��
 * @author zhixun
 *
 */
public class Fair {

	public static void main(String[] args) {
		// ��ƽ���ͷǹ�ƽ�� Ĭ�Ϸǹ�ƽ�� //ʵ�ʹ۲� �������򣬿��ܷǹ�ƽ���ҵĸ�����,�ǹ�ƽ����start���̲߳�һ���Ȼ������
		ReentrantLock lock = new ReentrantLock();
		for (int i = 0; i < 20; i++) {
			new Thread("�߳�" + i) {
				public void run() {
					System.out.println("�߳�" + getName() + "����");
					// synchronized ("test") {
					lock.lock();
					try {
						long l;
						sleep(l = (long) (Math.random() * 100));
						System.out.println(getName() + "�����" + l + "ms");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					lock.unlock();
					// }

				};
			}.start();
		}

	}

}
