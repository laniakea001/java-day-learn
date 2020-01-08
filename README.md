# day 21 线程池 submit 和 execute 方法区别

## 接收的参数不一样

```java
public class MainTest {

    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        pool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("execute");
            }
        });

        Future<?> submit = pool.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("submit");
            }
        });
        //等任务执行完毕会打印null
        System.out.println(submit.get());

        FutureTask<Integer> submit2 = (FutureTask<Integer>) pool.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("submit_2");
                return 2;
            }
        });
        System.out.println("result=" + submit2.get());
    }

}
```

## 返回值不一样

submit 有返回值 Future，而 execute 没有

## submit 方便 Exception 处理

```java
public class MainTest {

    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(10);

        Future submit = pool.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("submit");
                System.out.println(0/0);
            }
        });
        try {
            System.out.println("result=" + submit.get());
        }catch (Exception e){
            System.out.println(e);
        }
    }

}
```

# day 22 stack 实现一个 min 方法，O(1)的复杂度

- 原理同 day15

# day 23 数据库 3 个字段的联合索引，在用单字段时能否命中索引

1. 联合索引是由多个字段组成的索引。

2. 查询时使用联合索引的一个字段，如果这个字段在联合索引中所有字段的第一个，那就会用到索引，否则就无法使用到索引。

3. 联合索引 IDX(字段 A,字段 B,字段 C,字段 D)，当仅使用字段 A 查询时，索引 IDX 就会使用到；如果仅使用字段 B 或字段 C 或字段 D 查询，则索引 IDX 都不会用到。

4. 这个规则在 oracle 和 mysql 数据库中均成立。

5. 如果你经常要用到多个字段的多条件查询，可以考虑建立联合索引，一般是除第一个字段外的其它字段不经常用于条件筛选情况，比如说 a,b 两个字段，如果你经常用 a 条件或者 a+b 条件去查询，而很少单独用 b 条件查询，那么可以建立 a,b 的联合索引。如果 a 和 b 都要分别经常独立的被用作查询条件，那还是建立多个单列索引

# day 24 找到一个文件里出现次数最多的的数字，文件大小远大于内存容量

1. 1 个 32 位整数需要 4 个字节，1KB 可以存储 256 个数字，1GB 可以存储 2.6 亿个数字。如果只有 2 亿个数字，我们可能全部加载到内存。很明显，但是我们不可能开出一个以数字大小位下标的数组，因为数字可能非常大，从 1 到 100 亿的数字都可能存在
2. 如果我们是 Java 程序员，我们可以使用 HashMap 或者 TreeMap 等数据结构来维护一个 Map<数字，次数>的映射，但是 Map 占用的内存大小其实比较难以评估，用 2GB 来操作是 2 亿的数据是存在内存溢出的风险的。这个算法虽然简单，但却又一定的风险。
3. 其实并不难，我们对所有数字进行排序，常见的排序算法有冒泡排序、选择排序、堆排序、快速排序等。一般我们都使用快速排序，理论的算法复杂度位 O（NLogN），又稳定又快。
4. 问题就转化成，在一个有序的数组里面，如何快速找到出现次数最多的数字。同样非常简单，我们维护一个计数器，只要从左往右依次检查，如果一个数等于上一个数，计数器就加一，否则计数器就变成 1，重新开始统计。就可以巧妙地扫描一遍，找到出现次数最多地数字。
5. 那么如果文件特别大呢？像题目中说的 200 亿个数字的文件，我们不可能把所有的数字加载到内存里面，这种问题，其实都有一个常见的套路，那便是分治。
6. 什么是分治呢？就是把问题拆成多个子问题进行求解，例如上述问题，我们可以把这 200 亿个数，按照个位数的不同，分成 100 个不同的文件，然后采用上述算法进行统计。如果单个文件大小超过 2 亿个数字，那就继续拆分。
7. 把统计的数据直接存到文件上，原先某个数据的统计是直接在内存上进行加一，现在变成在文件上进行操作，这种做法的缺点就是 IO 次数会非常多，当然我们是不能浪费 2G 的内存的，我们可以把 2G 内存作为缓存，真正更新的时候再去写文件。

# day 25 给一个数组和一个整数，输出数组中所有和为给定整数的元素下标

- [详细代码](https://github.com/laniakea001/java-day-learn/tree/master/src/main/java/com/hjj/daylearn/javadaylearn/day25_sumInArrayIndex)
