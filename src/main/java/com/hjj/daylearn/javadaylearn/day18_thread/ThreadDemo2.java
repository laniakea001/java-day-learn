package com.hjj.daylearn.javadaylearn.day18_thread;

import java.util.ArrayList;
import java.util.List;

/**
 * 两个线程，一个只能存有数组1、2、3和另一个存有a、b、c，然后通过调度，最终结果输出1a2b3c
 */
public class ThreadDemo2 {
    private static volatile boolean isNum = false;

    public static void main(String[] args) {
        ThreadDemo2 demo2 = new ThreadDemo2();
        demo2.test();
    }

    public void test() {
        List strs = new ArrayList();
        MyThread3 thread1 = new MyThread3(strs);
        MyThread4 thread2 = new MyThread4(strs);
        Thread t1 = new Thread(thread1);
        Thread t2 = new Thread(thread2);
        t2.start();
        t1.start();
    }


    class MyThread4 implements Runnable {

        private char[] chars = {'a', 'b', 'c'};
        private List strs;

        public MyThread4(List strs) {
            this.strs = strs;
        }

        @Override
        public void run() {
            synchronized (this.strs) {
                for (int i = 0; i < chars.length; ) {

                    if (!isNum) {
                        this.strs.add(chars[i]);
                        isNum = true;
                        System.out.println(strs);

                        strs.notify();
                        i++;
                    } else {
                        try {
                            strs.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    class MyThread3 implements Runnable {

        private int[] chars = {1, 2, 3};
        private List strs;

        public MyThread3(List strs) {
            this.strs = strs;
        }

        @Override
        public void run() {
            synchronized (this.strs) {
                for (int i = 0; i < chars.length; ) {

                    if (isNum) {
                        this.strs.add(chars[i]);
                        isNum = false;
                        System.out.println(strs);

                        strs.notify();
                        i++;
                    } else {
                        try {
                            strs.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}


