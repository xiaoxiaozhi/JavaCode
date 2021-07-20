package com.zhixun.javacode.other;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test1 {
	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, 1);
		calendar.getTime();
		calendar.getTime().getTime();
		System.out.println("" + calendar.getTime().getTime());
		for (int i = 0; i < 5; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					List<String> lists = new ArrayList<String>();
					lists.add("12");
					System.out.println("main hashcode = " + lists.hashCode());
					sss(lists);
				}

			}).start();
		}

		Pattern p = Pattern.compile("[1]{4,}");
		Matcher m = p.matcher("112233111");

		System.out.println(m.find());

		new Thread("�߳� A") {
			public void run() {
				super.run();
				synchronized ("123") {
					System.out.println("123����waitִ��ǰ" + getName());
					try {
						"123".wait();// wait�����ͷ�ͬ������������һ�߳̿��Լ���ִ�� waitִ��ǰ
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("123����waitִ�к�" + getName());
				}
			};
		}.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		synchronized ("123") {
			"123".notify();// wait�����ͷ�ͬ������������һ�߳̿��Լ���ִ�� waitִ��ǰ
			System.out.println("�ͷ���");
		}
		new Thread() {
			public void run() {
				super.run();
				System.out.println("�������߳�");
			}
		}.start();

	}

	public static void sss(List<String> lists) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("sss hashcode = " + lists.hashCode());
				lists.get(0);

			}

		}).start();
	}
}
