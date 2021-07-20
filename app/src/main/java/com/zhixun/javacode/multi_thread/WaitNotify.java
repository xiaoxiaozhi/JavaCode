package com.zhixun.javacode.multi_thread;


import com.zhixun.javacode.module.Add;
import com.zhixun.javacode.module.Subtract;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.Date;


/**
 * 线程通信机制
 * 多个线程访问一个变量也可以通信，但不能实现等待通知的效果
 * wait()方法的作用，是释放同步锁，从运行状态退出，进入等待队列
 * wait(delay) delay毫秒后自动唤醒。 delay期间也可以由其他线程notify唤醒
 * notify()随机唤醒等待队列中，拥有一样同步锁的线程（指同步锁锁的对象一样）但是这个同步锁所在的实例不一样也行，使该线程退出等待队列进入可运行状态，notify方法只能唤醒一个线程
 * 例如类Test中有一个方法run1里面又有一个同步代码块synchronize("test")里面有有wait()。run2里面有一个同步块synchronize("test")里面有个notify
 *  线程A 执行实例test1的run1方法  线程B执行实例test2的run2方法。当run2的同步块执行完再执行run1的同步块。结论：等待同步机制跨线程跨实例
 * notifyAll()方法唤醒所有一样同步锁的线程 P157 线程状态图及线程状态讲解 http://wx4.sinaimg.cn/mw690/81366fa1gy1g30wtroh4pj20qr0ccwgt.jpg
 * join()主线程等待子线程结束再执行，join方法在内部是由wait方法实现的所以有释放锁的功能，sleep没有
 * ThreadLocal 存储线程的私有值
 * InheritableThreadLoacl 能够自定义子线程get初始值
 * 总结：1.执行完同步代码块会释放所对象
 * 2.同步代码块执行时遇到异常会释放所对象
 * 3.同步代码块执行wait方法会释放所对象，线程进入等待状态
 *
 */
public class WaitNotify {

	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			new Thread("线程" + i) {
				public void run() {
					super.run();
					synchronized ("123") {
						System.out.println("123对象wait执行前" + getName());
						try {
							"123".wait();// wait方法释放同步锁，所以下一线程可以继续执行 wait执行前
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("123对象wait执行后" + getName());
					}
				};
			}.start();
		}
		// 锁任一对象
		new Thread("A") {
			@Override
			public void run() {
				super.run();
				synchronized ("wait") {
					try {
						System.out.println("wait对象wait之前");
						"wait".wait();// 线程A执行到wait方法后自动释放同步锁，然后A线程进入等待队列。B线程才能继续执行
						System.out.println("wait对象wait之后");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("线程" + Thread.currentThread().getName() + "执行完毕");// 异步执行
			}
		}.start();
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Thread("B") {
			@Override
			public void run() {
				super.run();
				synchronized ("wait") {
					System.out.println("wait对象notify之前");
					"wait".notify();// notify释放相同同步锁对象的wait后，同步锁执行完才能 执行wait所在的同步块，并且一次只能随即通知一个线程，通知多个线程用notifyAll
					System.out.println("wait对象notify之后");
				}
				System.out.println("线程" + Thread.currentThread().getName() + "执行完毕");// 异步执行
			}
		}.start();
		new Thread("B.1") {
			@Override
			public void run() {
				super.run();
				synchronized ("123") {
					System.out.println("123对象notify之前");
					"123".notify();// notify释放相同同步锁对象的wait后，同步锁执行完才能 执行wait所在的同步块
					System.out.println("123对象notify之后");
				}
				System.out.println("线程" + Thread.currentThread().getName() + "执行完毕");// 异步执行
			}
		}.start();
		Object object = new Object();
		// 锁方法实际上是锁 synchronize(this)实例对象
		new Thread("C") {
			public void run() {
				new Test().testWait();
			};
		}.start();
		new Thread("D") {
			public void run() {
				new Test().testNotify();
			};
		}.start();
		// 演示interrupt引起 wait状态的线程异常
		Thread thread = new Thread("E") {
			public void run() {
				new Test().intterruptTest();
			}
		};
		thread.start();
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		thread.interrupt();
		// --------------过早通知-----------
		// 线程A执行wait 线程B执行notify，结果B线程先执行过早的通知，A线程得不到执行。
		// ---------------生产者消费者---------------
		Object object2 = new Object();
		for (int i = 0; i < 2; i++) {
			new Thread("线程--------------------------" + i) {
				public void run() {
					new Subtract(object2).subtrct();
				};
			}.start();
		}
		sleep(100);
		new Thread() {
			public void run() {
				new Add(object2).add();
			};
		}.start();

		// 管道流是一种特殊的流用于在线程间传送数据 PipedInputStream PipedOutputStream
		// PipedInputStream inputStream = new PipedInputStream();
		// PipedOutputStream outputStream = new PipedOutputStream();
		PipedWriter outputStream = new PipedWriter();
		PipedReader inputStream = new PipedReader();
		try {
			inputStream.connect(outputStream);// 上下两句作用一样，就是把输入和输出流关联起来
			// outputStream.connect(inputStream);//
			// 从打印结果可以发现，管道流先写后读，顺序与读操作和写操作的执行顺序无关，当输出流关闭后，输入流被触发。 是一种等待通知机制
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		new Thread() {
			public void run() {
				try {
					// byte[] bs = new byte[13];
					char[] bs = new char[7];// 字节流和字符流的写入写出区别，前者操作字节 byte[]后者操作char[]或string
					int result;
					while ((result = inputStream.read(bs)) != -1) {

						System.out.println(new String(bs, 0, result));
					}
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		new Thread() {
			public void run() {
				try {
					for (int i = 0; i < 10; i++) {
						// outputStream.write(("写入的数据:" + i).getBytes());
						outputStream.write(("写入的数据:" + i));
						// l = ("写入的数据:" + i).getBytes().length;
						// System.out.println("写");
					}
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();

		// 10个线程交替打印实心和空心的星星。线程交替执行 暂定感觉像是生产者消费者问题
		// join 主线程等待子线程执行结束再开始执行。
		Thread thread2 = new Thread("F") {
			public void run() {
				try {
					int r = (int) (Math.random() * 10000);
					System.out.println("睡眠" + r + "毫秒");
					sleep(r);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		};
		new Thread("G") {
			public void run() {
				// thread2.start();
				// thread2.join(); //F线程执行完毕，G线程才能执剩余部分。
			};
		}.start();
		thread2.start();
		try {
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("主线程再开始执行");

		new Thread("测试外层循环") {
			public void run() {
				for (int i = 0; i < 5; i++) {
					System.out.println("循环执行" + i + "次");
					synchronized ("测试外层循环") {
						try {
							"测试外层循环".wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
		}.start();
		// 生产者消费者问题 一对一
		new Thread("生产者") {
			public void run() {
				for (int i = 0; i < 10; i++) {
					new Producer().product();
				}
			};
		}.start();
		new Thread("消费者") {
			public void run() {
				for (int j = 0; j < 10; j++) {
					new Consumer().consumer();
				}
			};
		}.start();
		// 生产者消费者问题 多对多.只要把所有notify改成notifyAll,if变while就可以
		// for (int i = 0; i < 2; i++) {
		// new Thread("生产者" + i) {
		// public void run() {
		// for (int i = 0; i < 10; i++) {
		// new Producer().product();
		// }
		// };
		// }.start();
		// new Thread("消费者" + i) {
		// public void run() {
		// for (int j = 0; j < 10; j++) {
		// new Consumer().consumer();
		// }
		// };
		// }.start();
		// }
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 获取当前所有线程的状态
		Thread[] threads = new Thread[Thread.currentThread().getThreadGroup().activeCount()];
		Thread.currentThread().getThreadGroup().enumerate(threads);
		for (int i = 0; i < threads.length; i++) {
			System.out.println(threads[i].getName() + threads[i].getState());
		}
	}

	static class Test {
		public void testWait() {
			synchronized ("test") {
				try {
					System.out.println("字符串test的哈希值" + "test".hashCode());
					System.out.println("线程" + Thread.currentThread().getName() + "\twait前");
					"test".wait();// 线程A执行到wait方法后自动释放同步锁，然后A线程进入等待队列。B线程才能继续执行
					// Thread.currentThread().sleep(1000*1000);//同步锁 锁的是对象
					System.out.println("线程" + Thread.currentThread().getName() + "\twait后");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void testNotify() {
			synchronized ("test") {
				System.out.println("字符串test的哈希值" + "test".hashCode());
				System.out.println("线程" + Thread.currentThread().getName() + "\tnotify前");
				"test".notify();// notify释放相同同步锁对象的wait后，同步锁执行完才能 执行wait所在的同步块
				System.out.println("线程" + Thread.currentThread().getName() + "\tnotify后");
			}
		}

		// 处于wait状态的线程 执行interrupt会报异常 。锁被释放
		public void intterruptTest() {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.out.println("interrupt引起wait状态的线程异常，锁被释放----" + Thread.currentThread().getName());
				}
			}
		}
	}

	static class DbTools {
		private boolean flag = false;

		synchronized void printA() {
			// while (flag) {
			// try {
			// wait();
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// }
			System.out.println("☆☆☆☆☆");
			notifyAll();
		}

		synchronized void printB() {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("★★★★★");
		}
	}

	/**
	 *
	 *
	 */
	static class InheritableThreadLoacl extends InheritableThreadLocal {
		protected Object initialValue() {
			return new Date().getTime(); // 没有执行set()，get()的初始值是null。initialValue方法作用是给第一次get赋值。并一直保持，其它线程调用也是这个值
		};

		@Override
		protected Object childValue(Object parentValue) {
			return parentValue + "子线程";// 初始化子线程get初始值。 在子线程第一次调用get获得的是这个值，ThreadLocal没有这个方法
		}
	}

	public static ArrayList<String> listStr = new ArrayList<String>();

	static class Producer {
		void product() {
			synchronized (listStr) {
				while (listStr.size() != 0) {// 当变量有值等待消费者消耗
					try {
						System.out.println(Thread.currentThread().getName() + "等待");
						listStr.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				listStr.add("1");
				listStr.notify();
				System.out.println(
						Thread.currentThread().getName() + "_" + System.currentTimeMillis() + "_" + listStr.size());

			}
		}
	}

	static class Consumer {
		void consumer() {
			synchronized (listStr) {
				while (listStr.size() == 0) {// 当变量没有值等待生产者生产
					try {
						System.out.println(Thread.currentThread().getName() + "等待");
						listStr.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				listStr.remove(0);
				listStr.notify();// notify
				// 锁不释放，继续执行循环。第二次循环再入发现变量size=0等待生产者生产。（当内部同步代码块同步释放锁所在线程停止，外层循环也停止）
				System.out.println(
						Thread.currentThread().getName() + "_" + System.currentTimeMillis() + "_" + listStr.size());
			}
		}
	}

	static void sleep(long millis) {
		try {
			Thread.currentThread().sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
