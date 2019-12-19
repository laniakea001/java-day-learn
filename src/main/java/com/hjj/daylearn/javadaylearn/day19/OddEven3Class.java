package com.hjj.daylearn.javadaylearn.day19;

/**
 * 快慢指针
 */
public class OddEven3Class {

    public static void main(String args[]) {
        int[] a = new int[]{8, 4, 1, 6, 7, 4, 9, 6, 4};
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i]);
        }
        System.out.println();
        sort(a);
    }

    public static void sort(int arr[]) {
        int len = arr.length;
        int pre = 0;
        int pos = 0;
        boolean flag = true;
        for (; pos < len; pos++) {
            if (flag && arr[pos] % 2 == 0) {
                pre = pos;
                flag = false;//执行第(1)步
            }
            if (!flag && arr[pos] % 2 == 1) {
                arr[pos] = arr[pos] ^ arr[pre];
                arr[pre] = arr[pos] ^ arr[pre];
                arr[pos] = arr[pos] ^ arr[pre];
                pre++;//执行第(2)步，pos++在for循环里面执行了
            }
        }
        // 查看整个数组奇偶交换成功与否
        for (int j = 0; j < arr.length; j++) {
            System.out.print(arr[j]);
        }
    }
}
