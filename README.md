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
