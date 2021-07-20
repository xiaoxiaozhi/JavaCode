package com.zhixun.javacode.multi_thread;

import com.zhixun.javacode.module.Stub;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.net.ssl.SSLException;


/**
 * 设置JVM参数-server 鼠标放在该类，右键run as ---》 runConfigrations--->arguments---> -server
 * 试验volatile关键字 volatile 使变量在多个线程中可见。在eclipse中配置JVM的运行的运行参数为-server，线程为了效率一直在私有堆栈中取值，
 * 会发现多个线程操作一个实例是从私有堆栈中取得变量值，而不是从公共堆栈中取。也就是说，每个线程线程修改 同一个实例的变量即使是public修饰的，结果不能共享。（只对变量）
 * 这是因为每个线程线程修改的是私有堆栈变量的值。而不是公共变量 。各线程的数据值没有可视性造成的
 * 。volatile强制从公共堆栈取值。volatile增加了实例变量在多个线程间的可见性。java多线程编程核心 P136
 * synchronized其实就是一种非公平锁
 *
 */
public class SynchronizeThread {
	public static void main(String... args) {
		// 私有变量线程安全：创建一个类，里面有一个方法，在方法里面有一个私有变量int num，如果A线程执行num=100并打印,B线程执行num=200并打印
		// A线程执行结果 打印 num = 100,B线程执行结果num=200，B线程设置变量后没有覆盖A线程打印结果
		// 如果这个变量在类中，A打印结果会被b线程覆盖。个人猜测每次执行方法会重新开辟一块内存。 java多线程编程核心69页

		// synchronized 关键字 是对象锁，一个类中有有两个方法被
		// 标记。线程A执行a方法线程B执行b方法。要等到a方法执行完才能执行b方法。如果只有a方法被标记。B线程就可以异步调用b方法 P74页
		// 两个加了同步锁的方法1和方法2.线程执行完1之后也可以继续执行方法2叫锁重入。

		// 锁不具有继承性:子类执行自己的加锁方法后也可以执行父类的加锁方法。方法1在父类中加锁，子类覆写该方法没有加锁，子类执行该方法。结果非线程安全
		// synchronize 出现异常同步锁自动释放
		Stub stub = new Stub();
		// 如果Stub是一个非静态内部类实例化就会报错，这是因为内部类能访问外部类的变量，对外部类有一个隐式引用。
		// 变量也对实例有一个隐式引用，而静态方法不能访问实例。所以此时Stub初始化失败。
		new Thread("a") {
			@Override
			public void run() {
				super.run();
				stub.method1(); // 如果子类没有复写该方法，该方法是否同步看父类描述。如果覆盖了方法名前也没加锁则 不能同步。关键看覆盖后有没有在该方法名前加synchronize
			}
		}.start();
		new Thread("b") {
			@Override
			public void run() {
				super.run();
				stub.method1();
			}
		}.start();
		// synchronize 同步语句块。在一个方法中 操作1和操作3
		// 是非同步代码操作2是同步代码。两个线程会异步执行操作1。然后同步执行操作2.谁先执行完操作2谁就执行操作3
		// void test() {
		// //操作1
		// synchronized (this) {
		// //操作2
		// }
		// //操作3
		// }
		// 一个类中有太多加了同步锁的方法，会导致执行效率变慢。synchronized(非this)和synchronized(this)可以异步执行
		// 从打印结果可以看出来 两个线程执行snychronize(非this) 执行结果是同步顺序打印。
		// 一个类中有太多加了同步锁的方法，会导致执行效率变慢。synchronized(非this)和synchronized(this)可以异步执行
		// .注意synchronized(非this) 一定要是同一个对象才能实现同步
		// 加同步锁的静态方法，和普通方法里面有同步代码块，锁定的对象不一样前者是类，class锁可以对类的所有对象实例起作用，静态加锁方法实例化后，锁的对象还是class；后者是实例。
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 犯了一个错误，不能在这里加for循环。因为test1方法加锁是同步，for循环不同步，线程A循环一次就释放锁了。
				// for (int i = 0; i < 10; i++) {
				//
				new Test().test1();// 静态加锁方法实例化后锁的还是对象，所以线程A和线程B同步，从打印结果A结束和B开始时间一样可以看出来
				// Test.test1();
				// }

			}
		}, "A").start();

		new Thread(new Runnable() {

			@Override
			public void run() {
				new Test().test1();
				// Test.test1();
			}
		}, "B").start();
		// 演示volatile
		// volatile 使变量在多个线程中可见。从公共堆栈中取得变量值，而不是从线程私有堆栈中取。在eclipse中配置JVM的运行的运行参数为-server
		// 线程为了效率一直在私有堆栈中取值。
		// 但是在线程外面更新的确实公共堆栈变量的值。 所以一直死锁
		// 。volatile强制从公共堆栈取值。volatile增加了实例变量在多个线程间的可见性。java多线程编程核心 P136页
		// volatile 和 synchronize关键字比较 示例 SynchronizeThread.java
		PrintString pString = new PrintString();
		new Thread(pString).start();
		try {
			Thread.currentThread().sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		pString.setContinuePrint(false);
		// VolatileThread vThread = new VolatileThread();
		// vThread.start();
		// try {
		// Thread.currentThread().sleep(1);
		// } catch (InterruptedException e1) {
		// e1.printStackTrace();
		// }
		// vThread.setFlag(false);// 根本停不下来 。
		// System.out.println("flag 赋值为false");

		MyRunnable runnable = new MyRunnable();
		new Thread(runnable, "MyRunnable").start();
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		runnable.flag = false;
		// 用synchronize代替volatile
		MyRunnable3 runnable3 = new MyRunnable3();
		new Thread(runnable3, "MyRunnable3").start();
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		runnable3.flag = false;
		// 演示原子操作，实现同步
		MyRunnable1 myRunnable1 = new MyRunnable1();
		for (int i = 0; i < 5; i++) {
			new Thread(myRunnable1, "线程" + i).start();
		}

	}

	static class Test {
		private String name, pwd;
		static private String ss = "";

		public Test(String name, String pwd) {
			this.name = name;
			this.pwd = pwd;
		}

		public Test() {

		}

		static public void test() {
			synchronized (ss) {
				System.out.println("线程" + Thread.currentThread().getName() + "  begin" + System.currentTimeMillis());
				for (int i = 0; i < 999999999; i++) {
				}
				System.out.println("线程" + Thread.currentThread().getName() + "  end" + System.currentTimeMillis());
			}
		}

		/**
		 * 在线中调用方法Test.test1()。synchronize的锁对象是Test类。
		 * 在线程中调用new Test.test1() synchronize 锁的对象是一个实例
		 */
		synchronized static public void test1() {
			// try {
			// Thread.currentThread().sleep(50);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			System.out.println("线程" + Thread.currentThread().getName() + "  开始" + System.currentTimeMillis());
			for (long i = 0; i < 999999999; i++) {
			}
			System.out.println("线程" + Thread.currentThread().getName() + "  结束" + System.currentTimeMillis());
		}

		// 锁Test.class相当于静态方法锁.test1
		static public void test2() {
			synchronized (Test.class) {
				System.out.println("锁Test.class相当于静态方法锁test1");
			}
		}
	}

	// 测试volatile关键字
	static class MyRunnable implements Runnable {
		public boolean flag = true;

		@Override
		public void run() {
			System.out.println("开始演示volatile关键字---" + Thread.currentThread().getName());
			while (flag) {
//				System.out.println(Thread.currentThread().getName());//实际测试在这里执行代码后加不加 volatile 都没关系。如果没有代码测试结果和书中描述一致 甚是奇怪
			}
			System.out.println("演示结束---" + Thread.currentThread().getName());
		}
	}

	// 用synchronize代替volatile关键字
	static class MyRunnable3 implements Runnable {
		public boolean flag = true;

		@Override
		public void run() {
			System.out.println("开始演示synchronized代替volatile关键字---" + Thread.currentThread().getName());
			while (flag) {
				synchronized (new String()) {
					// synchronize关键字包含两个特征，同步性和可见性。 多个线程执行实例中一个方法。如果方法中使用了实例的变量，为了避免变量不可见
					// 要嘛给变量加volatile关键字，要嘛该方法有synchronize关键字修饰
				}
			}
			System.out.println("演示结束---" + Thread.currentThread().getName());
		}
	}

	/**
	 *1.原子操作是不可分割的整体 ,没有其他线程能中断正在原子操作中的变量,它可以在没有锁的情况下做到线程安全。  从打印信息可以看到 多个线程异步执行for循环。但是i的值却是同步。
	 *2.看 变量在内存中的工作过程.png  内存空间和线程空间
	 *3. incrementAndGet 和 incrementAndGet 之间异步。原子类 操作方法 和方法之间异步
	 */
	static class MyRunnable1 implements Runnable {
		public AtomicLong ai = new AtomicLong();

		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				System.out.println(Thread.currentThread().getName() + "\ti=" + ai.incrementAndGet()); // +1
			}
//			ai.addAndGet(10);// 按参数自加  方法和方法之间异步
			System.out.println("结果：" + ai.get());// 结果显示异步
			System.out.println("-----------" + Thread.currentThread().getName() + "--------------");
		}
	}

	static public class PrintString implements Runnable {
		private boolean isContinuePrint = true;

		public boolean isContinuePrint() {
			return isContinuePrint;
		}

		public void setContinuePrint(boolean isContinuePrint) {
			this.isContinuePrint = isContinuePrint;
		}

		public void printStringMethod() {
			while (isContinuePrint == true) {
				System.out.println("run print ");
			}
		}

		@Override
		public void run() {
			printStringMethod();
		}
	}
}
