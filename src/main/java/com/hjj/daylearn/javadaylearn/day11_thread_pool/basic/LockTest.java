package com.hjj.daylearn.javadaylearn.day11_thread_pool.basic;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description alibaba
 *
 *
 */
public class LockTest {
    private final static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        try {
            lock.tryLock();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
