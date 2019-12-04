package com.hjj.daylearn.javadaylearn.day11_thread_pool.mark.concurrent10;

import java.util.concurrent.TimeUnit;

/**
 * 一个同步方法可以调用另一个同步方法， 一个线程已经获取某个对象的锁，再次申请的时候仍然会得到
 * 该对象的锁也就是说synchronized获得的锁是可重入的
 *
 * 这里是继承中可能发生的情形，子类调用父类的同步方法， 锁定同一对象
 *
 */
public class T {
	synchronized void m1() {
		System.out.println("m1 start...");
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("m1 end...");
	}
	
	public static void main(String[] args) {
		new TT().m1();
	}
}

class TT extends T {
	@Override
	synchronized void m1() {
		System.out.println("child m1 start...");
		super.m1();
		System.out.println("child m1 end...");
	}
}
