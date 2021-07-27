package com.zhixun.javacode.other;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.transform.Templates;

public class Other {

	public static void main(String[] args1) {
		String[] args = new String[] { "23:00", "15:00", "16:00", "15:01" };
		long[] numbers = new long[args.length];
		for (int i = 0; i < args.length; i++) {
			numbers[i] = Long.valueOf(args[i].split(":")[0]) * 60 + Long.valueOf(args[i].split(":")[1]);
		}
		int i, j;
		for (i = 0; i < numbers.length - 1; i++) {
			for (j = 0; j < numbers.length - 1 - i; j++) {
				if (numbers[j] > numbers[j + 1]) {
					long temp = numbers[j];
					numbers[j] = numbers[j + 1];
					numbers[j + 1] = temp;
				}
			}
		}
		long k0 = 0l, k1 = 0l;
		long min = numbers[numbers.length - 1];
		for (int k = 0; k < numbers.length; k++) {
			if (k + 1 < numbers.length) {
				if (Math.abs(numbers[k] - numbers[k + 1]) < min) {
					min = Math.abs(numbers[k] - numbers[k + 1]);
					k0 = numbers[k];
					k1 = numbers[k + 1];
				}
			}
		}
		System.out.println("差值最小的两个时间" + k0 / 60 + ":" + k0 % 60 + " " + k1 / 60 + ":" + k1 % 60);
		System.out.println("得到的值：" + change(365));
	}

	public static void test() {
		System.out.print("test");

	}

	public static int change(int num) {
		System.out.println("传进来的数：" + num);
		String str = String.valueOf(num);// num为需要转化的整数
		if (str.length() == 1) {
			return num;
		}
		int[] tmp = new int[str.length()];
		for (int i = 0; i < str.length(); i++) {
			tmp[i] = Integer.parseInt(String.valueOf(str.charAt(i)));
		}
		int index = 0;
		int max = tmp[0];
		for (int i = 0; i < tmp.length; i++) {
			if (i + 1 < tmp.length) {
				if (max < tmp[i + 1]) {
					max = tmp[i + 1];
					index = i + 1;
					System.out.println("最大" + max + " 索引" + index);
				}
			}
		}
		if (index == 0) {
			StringBuilder sBuilder = new StringBuilder();
			for (int i = 1; i < tmp.length; i++) {
				sBuilder.append(tmp[i]);
			}
			System.out.println("sBuilder " + sBuilder.toString());
			return Integer.valueOf(
					String.valueOf(tmp[0]) + String.valueOf(change((int) (Integer.valueOf(sBuilder.toString())))));
		} else {
			tmp[0] = tmp[0] + tmp[index];
			tmp[index] = tmp[0] - tmp[index];
			tmp[0] = tmp[0] - tmp[index];
			StringBuilder sBuilder = new StringBuilder();
			for (int i = 0; i < tmp.length; i++) {
				sBuilder.append(String.valueOf(tmp[i]));
			}
			return (int) (Integer.valueOf(sBuilder.toString()));
		}
	}

}
