package com.zhixun.javacode.multi_thread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 定时器 1.timer.schedule(TimerTask,Date) 在某个时间执行任务 .执行后 线程一直存在 new
 * Timer(true)设置为守护线程 2.timer.schedule(TimerTask,Date,period) 在某个时间执行，并且
 * 每隔一段时间反复执行，如果任务耗时大于period，则按照任务结束时间开启下一次任务 3.timer.cancel()
 * 清空任务队列，有时不一定能执行，这是因为cancel没有争取到queue锁。 4.task.cancel() 从任务队列中移除自身。
 * 5.timer.schedule(TimerTask,delay) 延时执行
 * 6.timer.schedule(TimerTask,delay,period)延时并反复执行
 * 7.timer.scheduleAtFixedRate(TimerTask,Date,period)
 * .schedule一样所有任务顺序执行不存在线程安全问题，主要区别在不延时的情况
 *
 * @author zhixun
 *
 */
public class Timerr {

	public static void main(String[] args) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = "2019-05-27 16:31:00";// 如果这个时间早于 当前时间， timer会立即执行
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				System.out.println("执行时间" + format.format(new Date()));
				// cancel();// 从人物队列中移除自身。
			}
		};
		Timer timer = new Timer();
		// Timer timer = new Timer(true);// 设置为守护线程 主线程执行完，timer迅速结束。也就是说
		// 执行时间:XXX 这段可能打印不了
		try {
			timer.schedule(task, format.parse(dateStr));
			// 执行多个 task 并顺序执行
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					System.out.println("可以执行多个tasker，并顺序执行");
				}
			}, format.parse(dateStr));
			// 反复执行 当任务耗时大于 间隔时间。则任务完成后立即执行下一个任务
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					System.out.println("每隔一秒打印一次：" + format.format(new Date()));
					// sleep(2000);//打印结果变成了每隔两秒
				}
			}, new Date(), 1000);
			// timer.cancel();//清空任务队列，有时不一定能执行，这是因为cancel没有争取到queue锁。
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					System.out.println("以每秒一次的频率打印");
				}
			}, new Date(), 1000);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		timer.cancel();// 清空任务队列，有时不一定能执行，这是因为cancel没有争取到queue锁。

	}

	public static void sleep(long millis) {
		try {
			Thread.currentThread().sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
