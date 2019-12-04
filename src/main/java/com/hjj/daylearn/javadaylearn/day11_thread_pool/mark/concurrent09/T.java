package com.hjj.daylearn.javadaylearn.day11_thread_pool.mark.concurrent09;

import java.util.concurrent.TimeUnit;

/**
 * 一个同步方法可以调用另一个同步方法。 一个线程已经某个对象的锁，再次申请的时候任然会得到该对象的锁
 * 也就是说synchronized获得的锁是可重入的
 *
 */
public class T implements Runnable {

	@Override
	public synchronized void run() {
		System.out.println("m1 start...");
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		m2();
	}
	
	synchronized void m2() {
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("m2");
	}

	public static void main(String[] args) {
		T t = new T();
		new Thread(t::run).start();
	}
}
