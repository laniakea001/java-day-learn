package com.hjj.daylearn.javadaylearn.day03_lock.service.impl;

import com.hjj.daylearn.javadaylearn.day03_lock.mapper.OrderMapper;
import com.hjj.daylearn.javadaylearn.day03_lock.mapper.ProductMapper;
import com.hjj.daylearn.javadaylearn.day03_lock.service.BuyService;
import com.hjj.daylearn.javadaylearn.day03_lock.vo.Order;
import com.hjj.daylearn.javadaylearn.day03_lock.vo.Product;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class BuyServiceImpl implements BuyService {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 购买商品操作
     */
    @Override
    public String Buy() {
        /** 先判断是否有库存 */
        Product product = productMapper.selectByPrimaryKey(1);
        if (product.getNumber() <= 0) {
            System.out.println("库存不足！");
            return "库存不足！";
        } else {
            /** 减库存 */
            int i =productMapper.reduceOrder();
            if (i == 1) {
                /** 生成订单 */
                Order order = new Order();
                order.setOrderId(UUID.randomUUID().toString());
                orderMapper.insertSelective(order);
                System.out.println("购买成功！");
                return "购买成功！";
            } else {
                System.out.println("购买失败！");
                return "购买失败！";
            }
        }
    }

    @Override
    public String BuyOptimistic() {
        /** 先判断是否有库存 */
        Product product = productMapper.selectByPrimaryKey(1);
        if (product.getNumber() <= 0) {
            System.out.println("库存不足！");
            return "库存不足！";
        } else {
            /** 减库存 */
            int i = productMapper.reduceOrderOptimistic(product.getVersion());
            if (i == 1) {
                /** 生成订单 */
                Order order = new Order();
                order.setOrderId(UUID.randomUUID().toString());
                orderMapper.insertSelective(order);
                System.out.println("购买成功！");
                return "购买成功！";
            } else {
                System.out.println("购买失败！");
                return "购买失败！";
            }
        }
    }

    @Transactional
    @Override
    public String BuyPessimism() {

        /** 先判断是否有库存 */
//        Product product = productMapper.selectByPrimaryKey(1);
        Product product = productMapper.selectWithPessimism(1);
        if (product.getNumber() <= 0) {
            System.out.println("库存不足！");
            return "库存不足！";
        } else {
            /** 减库存 */
            int i =productMapper.reduceOrder();
            if (i == 1) {
                /** 生成订单 */
                Order order = new Order();
                order.setOrderId(UUID.randomUUID().toString());
                orderMapper.insertSelective(order);
                System.out.println("购买成功！");
                return "购买成功！";
            } else {
                System.out.println("购买失败！");
                return "购买失败！";
            }
        }
    }

    /**
     * 使用Redis锁 （常用方法向下看）
     * @return
     */
    @Override
    public String BuyRedis() {
        String key = "key_product";
        RLock lock = redissonClient.getLock(key);
        try {
            lock.lock();
            /** 先判断是否有库存 */
            Product product = productMapper.selectByPrimaryKey(1);
            if (product.getNumber() <= 0) {
                System.out.println("库存不足！");
                return "库存不足！";
            } else {
                /** 减库存 */
                int i =productMapper.reduceOrder();
                if (i == 1) {
                    /** 生成订单 */
                    Order order = new Order();
                    order.setOrderId(UUID.randomUUID().toString());
                    orderMapper.insertSelective(order);
                    System.out.println("购买成功！");
                    return "购买成功！";
                } else {
                    System.out.println("购买失败！");
                    return "购买失败！";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return "";
    }

    /**
     * redisson常用加锁方法
     */
    public void RedisLock() {
        RLock lock = redissonClient.getLock("anyLock");

        try {
            /** 1. 最常见的使用方法 */
            lock.lock();
            /** 2. 支持过期解锁功能,10秒钟以后自动解锁, 无需调用unlock方法手动解锁 */
            lock.lock(10, TimeUnit.SECONDS);

            /** 3. 尝试加锁，最多等待3秒，上锁以后10秒自动解锁 */
            boolean res = lock.tryLock(3, 10, TimeUnit.SECONDS);
            if (res) {
                /** do your business */
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
