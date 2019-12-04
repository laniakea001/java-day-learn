package com.hjj.daylearn.javadaylearn.day11_thread_pool.mark.concurrent01;

/**
 * synchronized关键字
 * 对对象加锁
 *
 */
public class T {

	private int count = 10;
	private static Object o = new Object();
	
	public void m() {
		synchronized(o) { // 任何线程要执行下面的代码， 必须先拿到o的锁(互斥锁)
			count --;
			System.out.println(Thread.currentThread().getName() + "" + count);
		}
	}
}
