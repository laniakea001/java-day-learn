package com.hjj.daylearn.javadaylearn.day18_thread;

import java.util.Arrays;

/**
 * 两个线程，一个只能存有数组1、2、3和另一个存有a、b、c，然后通过调度，最终结果输出1a2b3c
 */
public class ThreadDemo {
    public static void main(String[] args) {
        String[] strs = new String[6];
        MyThread1 thread1 = new MyThread1(strs);
        MyThread2 thread2 = new MyThread2(strs);
        Thread t1 = new Thread(thread1);
        Thread t2 = new Thread(thread2);
        t2.start();
        t1.start();


        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Arrays.toString(strs));
    }

}

class MyThread1 implements Runnable {

    private String[] strs;

    public MyThread1(String[] strs) {
        this.strs = strs;
    }

    private int[] arr = {1, 2, 3};
    private int curIndex = 0;

    @Override
    public void run() {

        for (int i = 0; i <= 4; i += 2) {
            synchronized (strs) {
                System.out.println(Thread.currentThread().getName()+"获取到锁，");
                strs[i] = arr[curIndex] + "";
                curIndex++;
                System.out.println(Thread.currentThread().getName()+"释放锁");
            }
        }
    }
}

class MyThread2 implements Runnable {

    private String[] strs;

    public MyThread2(String[] strs) {
        this.strs = strs;
    }

    private char[] arr = {'a', 'b', 'c'};
    private int curIndex = 0;

    @Override
    public void run() {

        for (int i = 1; i <= 5; i += 2) {
            synchronized (strs) {
                System.out.println(Thread.currentThread().getName()+"获取到锁，");
                strs[i] = arr[curIndex] + "";
                curIndex++;
                System.out.println(Thread.currentThread().getName()+"释放锁");
            }
        }
    }
}