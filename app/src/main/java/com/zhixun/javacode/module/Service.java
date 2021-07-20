package com.zhixun.javacode.module;

public class Service {
    //2��3��������1��4�Ƕ������� 3������ʵ�� ����ͬ��
	synchronized public void method1() {

	}

	synchronized static public void method2() {

	}

	public void method3() {
		synchronized (Service.class) {

		}
	}

	public void method4() {
		synchronized (this) {

		}
	}

}
