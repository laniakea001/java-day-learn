package com.hjj.daylearn.javadaylearn.day11_thread_pool.basic;

/**
 * @description alibaba
 *
 *
 */
public class FloatWrapTest {
    public static void main(String[] args) {
        Float a = Float.valueOf(1.0f - 0.9f);
        Float b = Float.valueOf(0.9f - 0.8f);
        if (a == b) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }
}
