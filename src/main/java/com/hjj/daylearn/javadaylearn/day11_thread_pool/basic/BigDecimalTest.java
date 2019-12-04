package com.hjj.daylearn.javadaylearn.day11_thread_pool.basic;

import java.math.BigDecimal;

/**
 * @description alibaba
 *
 *
 */
public class BigDecimalTest {
    public static void main(String[] args) {
        BigDecimal a = new BigDecimal(0.1);
        System.out.println(a);

        // 推荐使用
        BigDecimal b = new BigDecimal("0.1");
        System.out.println(b);
    }
}
