package com.zhixun.javacode.module;

public class VolatileThread extends Thread {
	private boolean flag = true;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	@Override
	public void run() {
		System.out.println("进入run---------");
		while (flag) {
//			System.out.println("循环---------");
		}
		System.out.println("线程停止-------");
	}
}
