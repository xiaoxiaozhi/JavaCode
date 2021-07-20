package com.zhixun.javacode.module;

public class PrintString implements Runnable {
	private boolean isContinuePrint = true;

	public boolean isContinuePrint() {
		return isContinuePrint;
	}

	public void setContinuePrint(boolean isContinuePrint) {
		this.isContinuePrint = isContinuePrint;
	}

	public void printStringMethod() {
		while (isContinuePrint == true) {
			System.out.println("run print " );
		}
	}

	@Override
	public void run() {
		printStringMethod();
	}
}
