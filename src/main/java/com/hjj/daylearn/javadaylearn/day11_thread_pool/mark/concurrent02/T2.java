package com.hjj.daylearn.javadaylearn.day11_thread_pool.mark.concurrent02;

/**
 * synchronized 对类加锁
 *
 */
public class T2 implements Runnable {

	private static int count = 10;

	@Override
	public void run() {
		// lock current object.
		synchronized(T2.class) {
			count --;
			System.out.println(Thread.currentThread().getName() + "" + count);
		}
	}

	public static void main(String[] args) {
		for (int i=0; i<10; i++) {
			new Thread(new T2(), "thread" + i + "-").start();
		}
	}

}
