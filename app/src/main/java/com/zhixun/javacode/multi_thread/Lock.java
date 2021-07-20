package com.zhixun.javacode.multi_thread;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**1.ReentrantLock 可重入锁  像synchronize一样有同步功能，同时还有多路分支、嗅探锁定
 * 2.使用condition实现等待/通知  。synchronize与wait和notify结合可以实现等待通知功能，condition和reentrantLock结合也能实现等待通知功能
 *   后者可以选择被唤醒的线程
 * 3.公平锁和不公平锁，前者按照先进先出的FIFO顺序先加锁先执行（基本是这样仍然会有例外），后者随机抢占。synchronized其实就是一种非公平锁
 * 4.lock.getHoldCount 当前线程保持此锁的个数，调用lock方法的次数。参考ProducerConsumer。从打印结果看lock.lock每执行一次lock.getHoldCount()就加一lock.unlock执行一次就减一
 * 5.getQueueLength 获取正在等待锁的线程数量,
 * 6.lock.getWaitQueueLength(condition) 获取处于waiting状态的线程数量，需要在lock.lock lock.unlock中执行
 * 7.lock.hasQueuedThread(thread) 检查该线程是否在等待 该锁。如果被锁的线程还未执行。调用该方法返回false
 * 8.lock.hasWaiters(condition) 检查是否有线程处于waiting状态，需要在lock.lock lock.unlock中执行
 * 9.lock.isHeldByCurrentThread().当前线程是否使用此锁
 * 10.lock.isLocked() 锁是否被线程使用。从结果看，线程使用锁前调用返回false 使用中返回true 使用后没有线程再使用该锁返回false
 * 11.lock.lockInterruptibly();//如果当前线程未被中断则获取锁，已经中断则抛出异常。也就是说，thread.interrupt()先于lock.lockInterruptibly()执行。后者抛异常
 * 12.lock.tryLock() 线程AB同时执行。线程A获取锁返回true 此时线程B没有获取锁返回false
 * 13.lock3.tryLock(3, TimeUnit.SECONDS) 。线程A执行tryLock(3, TimeUnit.SECONDS)。由于设置了时间3秒后获取该锁，线程B随即执行后获取锁。
 *    就是说线程A在规定时间呢锁可以被剥夺
 * 14.condation.awaitUninterruptibly();// interrupt 打断不走异常
 * 15.condition2.awaitUntil(Date deadline) 在某个日期结束等待。以下是获取Date的代码
 *  Calendar calendar = Calendar.getInstance();calendar.add(Calendar.SECOND, 10);calendar.getTime()
 * 16.读写锁  读操作相关的锁叫共享锁，写操作相关的叫排它锁。读锁之间不互斥，写锁和写锁、写锁和读锁之间互斥
 */
public class Lock {

	public static void main(String[] args) {
		MyServer server1 = new MyServer();
		MyServer server2 = new MyServer();
		new Thread("A") {
			public void run() {
				server1.methodA();
			};
		}.start();
		new Thread("B") {
			public void run() {
				server2.methodA();
			};
		}.start();
		ProducerConsumer pConsumer = new ProducerConsumer();
		// new ThreadA(pConsumer).start();
		// new ThreadB(pConsumer).start();
		// 单一生产者和消费者
		new Thread("生产者") {
			public void run() {
				for (int i = 0; i < 10; i++) {
					pConsumer.product();
				}
			};
		}.start();
		new Thread("消费者") {
			public void run() {
				for (int i = 0; i < 10; i++) {
					pConsumer.consumer();
				}
			};
		}.start();
		// 多个生产者和消费者
		// for (int i = 0; i < 2; i++) {
		// new Thread("生产者" + i) {
		// public void run() {
		// for (int i = 0; i < 10; i++) {
		// pConsumer.product();
		// }
		// };
		// }.start();
		// new Thread("消费者" + i) {
		// public void run() {
		// for (int i = 0; i < 10; i++) {
		// pConsumer.consumer();
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
		// getQueueLength 获取正在等待锁的线程数量
		long start = System.currentTimeMillis();
		System.out.println("---------------------");
		ReentrantLock lock = new ReentrantLock();
		for (int i = 0; i < 10; i++) {
			new Thread() {
				public void run() {
					lock.lock();
					try {
						sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					lock.unlock();
				};
			}.start();
		}
		System.out.println("等待时间" + (System.currentTimeMillis() - start) + "ms");
		System.out.println("等待获取锁的线程数量" + lock.getQueueLength());
		// getWaitQueueLength
		// 获取处于waiting状态的线程数量。有个疑问，在同步代码块中调用lock.getWaitQueueLength(condition))为什么会被执行，因为之前的线程可都是等待状态.原因是await释放同步锁
		ReentrantLock rLock = new ReentrantLock();
		Condition condition = rLock.newCondition();
		for (int i = 0; i < 5; i++) {
			new ThreadC(i) {
				@Override
				public void run() {
					super.run();
					rLock.lock();
					try {
						System.out.println(getName() + " " + System.currentTimeMillis());
						condition.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					rLock.unlock();
				};
			}.start();
		}

		System.out.println(Thread.currentThread().getName() + " " + System.currentTimeMillis());
		rLock.lock();
		System.out.println("有线程处于waiting状态吗" + rLock.hasWaiters(condition));
		System.out.println("处于waiting状态的线程数量" + rLock.getWaitQueueLength(condition));
		rLock.unlock();
		// rLock.hasQueuedThread(thread) //检查线程是否在等待
		ReentrantLock reentrantLock = new ReentrantLock();
		Thread thread = new Thread("线程C") {
			public void run() {
				reentrantLock.lock();
				System.out.println(getName() + "开始执行");
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				reentrantLock.unlock();
			};

		};

		Thread thread2 = new Thread("线程D") {
			public void run() {
				reentrantLock.lock();
				System.out.println(getName() + "开始执行");
				reentrantLock.unlock();
			};
		};
		thread.start();
		thread2.start();
		try {
			Thread.sleep(100);// 让线程C和D先执行，否则hasQueuedThread 返回false
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(thread.getName() + "是否在等待" + reentrantLock.hasQueuedThread(thread) + " 是否有等待的线程"
				+ reentrantLock.hasQueuedThreads());
		System.out.println(thread2.getName() + "是否在等待" + reentrantLock.hasQueuedThread(thread2) + "是否有等待的线程"
				+ reentrantLock.hasQueuedThreads());
		ReentrantLock rLock2 = new ReentrantLock();
		new Thread("线程E") {
			public void run() {
				System.out.println("锁是否被使用1" + rLock2.isLocked());
				System.out.println(getName() + "是否使用此锁" + rLock2.isHeldByCurrentThread());
				rLock2.lock();
				System.out.println("锁是否被使用2" + rLock2.isLocked());
				System.out.println(getName() + "是否使用此锁" + rLock2.isHeldByCurrentThread());
				rLock2.unlock();
			};
		}.start();
		sleep(1000);
		System.out.println("锁是否被使用3" + rLock2.isLocked());// 从打印结果看。没有线程持有锁返回false
		// lock.lockInterruptibly() 线程执行thread3.interrupt() 打了个标记，随后执行lockInterruptibly
		// 报异常
		ReentrantLock reentrantLock2 = new ReentrantLock();
		Thread thread3 = new Thread() {
			public void run() {
				try {
					reentrantLock2.lockInterruptibly();// interrupt执行后，线程获取锁则报异常
					System.out.println("lockInterruptibly 试验");
				} catch (InterruptedException e) {
					// interrupt 中断锁异常
					e.printStackTrace();
				} finally {
					if (reentrantLock2.isHeldByCurrentThread()) {
						reentrantLock2.unlock();// 线程产生异常丢失锁
					}
				}
			};
		};
		thread3.setPriority(1);
		thread3.start();
		System.out.println("interrupt触发锁中断");
		thread3.interrupt();
		// lock.tryLock()
		ReentrantLock lock2 = new ReentrantLock();
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				if (lock2.tryLock()) {// 如果获取锁返回true。锁被其它线程持有则返回false
					System.out.println(Thread.currentThread().getName() + "获取锁        " + System.currentTimeMillis());
				} else {
					System.out.println(Thread.currentThread().getName() + "没有获取锁 " + System.currentTimeMillis());
				}
				if (lock2.isHeldByCurrentThread()) {
					System.out.println(Thread.currentThread().getName() + "释放锁        " + System.currentTimeMillis());
					lock2.unlock();
				}
			}
		};
		new Thread(runnable, "线程4").start();
		new Thread(runnable, "线程5").start();
		// lock.tryLock(timeOut,TimeUnit) 在规定时间内没有第二个线程需要锁，则第一个线程获取该锁。有点像延迟获取锁。
		// 从打印结果看线程6先执行tryLock，线程7随即也执行tryLock。由于设置了延时时间。线程7获取锁
		sleep(1000);
		ReentrantLock lock3 = new ReentrantLock();
		Runnable runnable2 = new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println(Thread.currentThread().getName() + "执行前        " + System.currentTimeMillis());
					if (lock3.tryLock(3, TimeUnit.SECONDS)) {
						System.out
								.println(Thread.currentThread().getName() + "获取锁        " + System.currentTimeMillis());
					} else {
						System.out.println(Thread.currentThread().getName() + "没有获取锁 " + System.currentTimeMillis());
					}
					if (lock2.isHeldByCurrentThread()) {
						System.out
								.println(Thread.currentThread().getName() + "释放锁        " + System.currentTimeMillis());
						lock2.unlock();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(runnable2, "线程6").start();
		new Thread(runnable2, "线程7").start();
		ReentrantLock lock4 = new ReentrantLock();
		Condition condition2 = lock4.newCondition();
		Thread thread4 = new Thread(() -> {
			lock4.lock();
			System.out.println("awaitUninterruptibly执行前" + System.currentTimeMillis());
			condition2.awaitUninterruptibly();// interrupt 打断不走异常
			System.out.println("awaitUninterruptibly执行后" + System.currentTimeMillis());
			lock4.unlock();
		}, "");
		thread4.start();
		sleep(1000);
		thread4.interrupt();
		// 读写锁
		Runnable runnable3 = new Runnable() {
			// 两个线程同时竞争读锁，异步
			ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

			@Override
			public void run() {
				readWriteLock.readLock().lock();
				sleep(1000);
				System.out.println(Thread.currentThread().getName() + "获取读锁_" + System.currentTimeMillis());
				readWriteLock.readLock().unlock();
			}
		};
		new Thread(runnable3, "线程8").start();
		new Thread(runnable3, "线程9").start();// 从打印结果看 线程8和线程9同时执行，说明读操作共享锁线程异步 不互斥。

		Runnable runnable4 = new Runnable() {
			// 两个线程同时竞争写锁 互斥
			ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

			@Override
			public void run() {
				readWriteLock.writeLock().lock();
				sleep(1000);
				System.out.println(Thread.currentThread().getName() + "获取写锁_" + System.currentTimeMillis());
				readWriteLock.writeLock().unlock();
			}
		};
		new Thread(runnable4, "线程10").start();
		new Thread(runnable4, "线程11").start();// 从打印结果看 线程10和线程11同时竞争锁是互斥。
		// 从打印结果看 读写操作 互斥，同理写读操作也互斥
		ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
		new Thread("线程12") {
			public void run() {
				readWriteLock.readLock().lock();
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(getName() + "获取读锁_" + System.currentTimeMillis());
				readWriteLock.readLock().unlock();
			};
		}.start();
		new Thread("线程13") {
			public void run() {
				readWriteLock.writeLock().lock();
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + "获取写锁_" + System.currentTimeMillis());
				readWriteLock.writeLock().unlock();
			};
		}.start();
	}

	static class MyServer {
		static ReentrantLock lock = new ReentrantLock();// 从打印结果，持有的锁对象是自己
		// ReentrantLock lock = new ReentrantLock();

		public void methodA() {
			try {
				lock.lock();
				System.out.println("methodA开始 " + Thread.currentThread().getName() + " " + System.currentTimeMillis());
				Thread.currentThread().sleep(1000);
				System.out.println("methodA结束 " + Thread.currentThread().getName() + " " + System.currentTimeMillis());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

		public void methodB() {
			try {
				lock.lock();
				System.out.println("methodB开始 " + Thread.currentThread().getName() + " " + System.currentTimeMillis());
				Thread.currentThread().sleep(1000);
				System.out.println("methodB结束 " + Thread.currentThread().getName() + " " + System.currentTimeMillis());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}

	static class ThreadA extends Thread {
		private ProducerConsumer pConsumer;

		public ThreadA(ProducerConsumer pc) {
			this.pConsumer = pc;
		}

		@Override
		public void run() {
			super.run();
			for (int i = 0; i < 10; i++) {
				pConsumer.product();
			}
		}
	}

	static class ThreadB extends Thread {
		private ProducerConsumer pConsumer;

		public ThreadB(ProducerConsumer pc) {
			this.pConsumer = pc;
		}

		@Override
		public void run() {
			super.run();
			for (int i = 0; i < 10; i++) {
				pConsumer.consumer();
			}
		}
	}

	static class ThreadC extends Thread {
		public int num;

		public ThreadC(int num) {
			this.num = num;
		}
	}

	public static class ProducerConsumer {
		private ReentrantLock reentrantLock = new ReentrantLock();
		private Condition condition = reentrantLock.newCondition();
		private ArrayList<String> listStr = new ArrayList<String>();
		private boolean hasValue = false;

		void product() {
			try {
				reentrantLock.lock();
				// reentrantLock.lock();
				// reentrantLock.lock();
				while (listStr.size() != 0) {
					// while (hasValue == true) {
					condition.await();// 注意是await 不是 wait
				}
				listStr.add("1");
				System.out.println(
						Thread.currentThread().getName() + "_" + System.currentTimeMillis() + "_" + listStr.size());
				System.out.println("reentrantLock被调用次数" + reentrantLock.getHoldCount());
				// hasValue = true;
				condition.signalAll();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				reentrantLock.unlock();
				// reentrantLock.unlock();
				// reentrantLock.unlock();
			}

		}

		void consumer() {
			try {
				reentrantLock.lock();
				while (listStr.size() == 0) {
					// while (hasValue == false) {
					condition.await();
				}
				listStr.remove(0);
				System.out.println(
						Thread.currentThread().getName() + "_" + System.currentTimeMillis() + "_" + listStr.size());
				// System.out.println("reentrantLock被调用次数" + reentrantLock.getHoldCount());
				// hasValue = false;
				condition.signalAll();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				reentrantLock.unlock();
			}
		}
	}

	public static void sleep(long millis) {
		try {
			Thread.currentThread().sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
