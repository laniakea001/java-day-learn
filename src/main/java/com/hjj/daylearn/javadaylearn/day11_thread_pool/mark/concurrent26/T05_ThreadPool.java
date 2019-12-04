package com.hjj.daylearn.javadaylearn.day11_thread_pool.mark.concurrent26;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 固定线程个数的线程池
 *
 */
public class T05_ThreadPool {
	public static void main(String[] args) throws InterruptedException {
		// 固定的
		ExecutorService service = Executors.newFixedThreadPool(9);
		for (int i = 1; i <= 10; i ++) {
			service.execute(()->{
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName());
			});
		}
		
		System.out.println(service);

		// 关闭线程池
		service.shutdown();
		System.out.println(service.isTerminated());
		System.out.println(service.isShutdown());

		System.out.println(service);
		
		TimeUnit.SECONDS.sleep(5);
		System.out.println(service.isTerminated());
		System.out.println(service.isShutdown());

		System.out.println(service);
	}
}