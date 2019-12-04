package com.hjj.daylearn.javadaylearn.day11_thread_pool.mark.concurrent26;

import java.util.concurrent.Executor;

/**
 * Executor接口
 *
 */
public class T01_MyExecutor implements Executor {

	@Override
	public void execute(Runnable command) {
		// new Thread(command).run();
		command.run();
	}
	
	public static void main(String[] args) {
		new T01_MyExecutor().execute(() -> System.out.println("hello executor"));
	}
	
}