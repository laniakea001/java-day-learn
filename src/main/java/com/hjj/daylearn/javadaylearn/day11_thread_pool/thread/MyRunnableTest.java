package com.hjj.daylearn.javadaylearn.day11_thread_pool.thread;

/**
 *
 */
public class MyRunnableTest {
    public static void main(String[] args) {
        MyRunnable runnable = new MyRunnable();
        Thread thread1 = new Thread(runnable, "线程1");
        Thread thread2 = new Thread(runnable, "线程2");
        Thread thread3 = new Thread(runnable, "线程3");

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
