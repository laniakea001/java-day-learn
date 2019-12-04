package com.hjj.daylearn.javadaylearn.day11_thread_pool.thread;

/**
 *
 */
public interface AnimalBehavior {
    default void eat() {
        System.out.println("hello world");
    }
}
