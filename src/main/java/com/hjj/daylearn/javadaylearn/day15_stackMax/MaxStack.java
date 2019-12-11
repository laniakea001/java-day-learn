package com.hjj.daylearn.javadaylearn.day15_stackMax;

import java.util.Stack;

/**
 * 维护2个栈： 1）原数据栈： 遍历push进去 2）最大值栈： push原数据栈时，每次peek最大值栈比较，大于的，push进入最大值栈
 */
public class MaxStack {

    public static void main(String[] args) {

        int[] data = { 4, 5, 13, 2, 1 };

        Stack<Integer> stackData = new Stack<Integer>();
        Stack<Integer> stackMax = new Stack<Integer>();

        for (int i = 0; i < data.length; i++) {
            if (stackMax.empty())
                stackMax.push(data[i]);
            if (stackMax.peek() < data[i]) {
                stackMax.push(data[i]);
            }
            stackData.push(data[i]);
        }
        System.out.println(stackData);
        System.out.println(stackMax);
        System.out.println("最大值：" + stackMax.peek());
    }
}