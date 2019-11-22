package com.hjj.daylearn.javadaylearn.day03_lock.service;

public interface BuyService {


    /**
     * 原始购买
     * @return
     */
    String Buy();

    /**
     * 使用乐观锁
     * @return
     */
    String BuyOptimistic();

    /**
     * 使用悲观锁
     * @return
     */
    String BuyPessimism();

    /**
     * 使用分布式锁
     * @return
     */
    String BuyRedis();
}
