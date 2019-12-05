package com.hjj.daylearn.javadaylearn.day12_limit_req;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 计数器算法
 */
public class AtomicIntegerServiceTask implements Runnable {

    private static AtomicInteger count = new AtomicInteger(0);

    private String serviceName;

    AtomicIntegerServiceTask(String serviceName) {
        this.serviceName = serviceName;
    }

    public void run() {
        if (count.get() >= 5) {
            System.out.println("请求用户过多，请稍后在试！");
        } else {
            try {
                System.out.println("Thread " + serviceName + " is working");
                count.incrementAndGet();
                Thread.sleep(1000);
                System.out.println("Thread " + serviceName + " is over");
            } catch (InterruptedException e) {
                System.out.println("error ...." + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
        for (int i = 1; i <= 100; i++) {
            AtomicIntegerServiceTask serviceTask = new AtomicIntegerServiceTask("task" + i);
            executor.submit(serviceTask);
        }
    }
}