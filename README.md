# day 11 线程池

Java 中的线程池是运用场景最多的并发框架，几乎所有需要异步或并发执行任务的程序 都可以使用线程池。在开发过程中，合理地使用线程池能够带来 3 个好处。

- 第一：降低资源消耗。通过重复利用已创建的线程降低线程创建和销毁造成的消耗。

- 第二：提高响应速度。当任务到达时，任务可以不需要等到线程创建就能立即执行。

- 第三：提高线程的可管理性。线程是稀缺资源，如果无限制地创建，不仅会消耗系统资源， 还会降低系统的稳定性，使用线程池可以进行统一分配、调优和监控。

## 线程池的组成

Java 中线程池用 ThreadPoolExecutor 来创建

```java
public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler)
```

其中的参数为：

- int corePoolSize：必需参数，规定了线程池的基本大小，当提交一个任务到线程池时，线程池会创建一个线 程来执行任务，即使其他空闲的基本线程能够执行新任务也会创建线程，等到需要执行的任务数大于线程池基本大小时就不再创建。如果调用了线程池的 prestartAllCoreThreads()方法， 线程池会提前创建并启动所有基本线程。
- int maximumPoolSize：必需参数，线程池中允许创建线程的最大数量，如果队列满了，并且已创建的线程数小于最大线程数，则线程池会再创建新的线程执行任务。值得注意的是，如 果使用了无界的任务队列这个参数就没什么效果。
  long keepAliveTime：必需参数，线程活动保持时间，线程池的工作线程空闲后，保持存活的时间。所以，如果任务很多，并且每个任务执行的时间比较短，可以调大时间，提高线程的利用率。
- TimeUnit unit：必需参数，线程活动保持时间的单位，可选的单位有天（DAYS）、小时（HOURS）、分钟 （MINUTES）、毫秒（MILLISECONDS）、微秒（MICROSECONDS，千分之一毫秒）和纳秒 （NANOSECONDS，千分之一微秒）。
- BlockingQueue<Runnable> workQueue：必需参数，任务队列，用于保存等待执行的任务的阻塞队列，关于阻塞队列可以参考https://my.oschina.net/u/3352298/blog/1807780
- ThreadFactory threadFactory：非必须参数，不设置此参数会采用内置默认参数。用于设置创建线程的工厂，可以通过线程工厂给每个创建出来的线程设置更有意义的名字。
- RejectedExecutionHandler handler：非必须参数，不设置此参数会采用内置默认参数，设置饱和策略，当队列和线程池都满了，说明线程池处于饱和状态，那么必须采取一种策略处理提交的新任务。RejectedExecutionHandler 的实现类在 ThreadPoolExecutor 中有四个静态内部类，这个策略默认情况下是 AbortPolicy，表示无法 处理新任务时抛出异常。共有 4 中策略，包括 AbortPolicy（直接抛出异常）。CallerRunsPolicy（只用调用者所在线程来运行任务）。DiscardOldestPolicy（丢弃队列里最近的一个任务，并执行当前任务）。DiscardPolicy（不处理，丢弃掉）。也可以根据应用场景需要来实现 RejectedExecutionHandler 接口自定义策略。如记录日志或持久化存储不能处理的任务。

## 线程池的图解

![Image text](https://raw.githubusercontent.com/laniakea001/java-day-learn/master/src/main/resources/static/readMeImage/线程池的原理图.png)
依据图片 ThreadPoolExecutor 执行 execute()方法有四种情况：

1. 如果当前运行的线程少于 corePoolSize，则创建新线程来执行任务（注意，执行这一步骤 需要获取全局锁）。
2. 如果运行的线程等于或多于 corePoolSize，则将任务加入 BlockingQueue。
3. 如果无法将任务加入 BlockingQueue（队列已满），则创建新的线程来处理任务（注意，执 行这一步骤需要获取全局锁）。
4. 如果创建新线程将使当前运行的线程超出 maximumPoolSize，任务将被拒绝，并调用 RejectedExecutionHandler.rejectedExecution()方法。

## 补充

### 调度线程池图解

![Image text](https://raw.githubusercontent.com/laniakea001/java-day-learn/master/src/main/resources/static/readMeImage/调度线程池图解.png)

### 各种类型的线程池

- [详细代码](https://github.com/laniakea001/java-day-learn/tree/master/src/main/java/com/hjj/daylearn/javadaylearn/day11_thread_pool)

1. FixedThreadPool:创建使用固定线程数的 FixedThreadPool 的 API，适用于为了满足资源管理的需求，而需要限制当前线程数量的应用场 景，它适用于负载比较重的服务器。

2. SingleThreadExecutor:创建使用单个线程的 SingleThreadExecutor 的 API，适用于需要保证顺序地执行各个任务；并且在任意时间点，不会有多 个线程是活动的应用场景。

3. CachedThreadPool:创建一个会根据需要创建新线程的 CachedThreadPool 的 API，是大小无界的线程池，适用于执行很多的短期异步任务的小程序，或者是负载较轻的服务器。

4. ScheduledThreadPoolExecutor:内置 timer 定时器，使用工厂类 Executors 来创建的包含一个或多个线程的线程池。适用于执行周期任务；

5. ForkJoinPool：将一个大任务拆分成多个小任务后，使用 fork 可以将小任务分发给其他线程同时处理，使用 join 可以将多个线程处理的结果进行汇总；这实际上就是分治思想的并行版本。

6. WorkStealingPool：任务窃取，都是守护线程。每个线程都有要处理的队列中的任务，如果其中的线程完成自己队列中的任务，那么它可以去其他线程中获取其他线程的任务去执行。

# day 12 限流算法

- [详细代码](https://github.com/laniakea001/java-day-learn/tree/master/src/main/java/com/hjj/daylearn/javadaylearn/day12_limit_req)

## 固定窗口计数器

![Image text](https://raw.githubusercontent.com/laniakea001/java-day-learn/master/src/main/resources/static/readMeImage/固定窗口计数器.webp)

- 固定窗口计数器算法将时间线划分为固定大小的窗口，并为每个窗口分配计数器。 每个请求根据其到达时间映射到一个窗口。 如果窗口中的计数器已达到限制，则应拒绝落入此窗口的请求。 例如，如果我们将窗口大小设置为 1 分钟。 然后窗口是[00:00, 00:01), [00:01, 00:02), ...[23:59, 00:00)。 假设限制是每分钟 2 个请求：

- 00:00:24 的请求属于窗口 1，它将窗口的计数器增加到 1.下一个请求在 00:00:36 也属于窗口 1，窗口的计数器变为 2.下一个请求来自 00:00:49 因为计数器超出限制而被拒绝。 然后可以提供 00:01:12 的请求，因为它属于窗口 2。

## 滑动窗口日志

![Image text](https://raw.githubusercontent.com/laniakea001/java-day-learn/master/src/main/resources/static/readMeImage/滑动窗口日志.webp)
滑动窗口日志算法为每个用户保留请求时间戳的日志。 当请求到来时，我们首先弹出所有过时的时间戳，然后将新的请求时间附加到日志中。 然后我们根据日志大小是否超出限制来决定是否应该处理此请求。 例如，假设速率限制是每分钟 2 个请求：

## 滑动窗口

![Image text](https://raw.githubusercontent.com/laniakea001/java-day-learn/master/src/main/resources/static/readMeImage/滑动窗口.webp)

- 滑动窗口计数器类似于固定窗口计数器，但它通过在前一窗口中将加权计数添加到当前窗口中的计数来平滑流量突发。 例如，假设限制为每分钟 10 次。 窗口[00:00, 00:01)中有 9 个请求，窗口[00:01, 00:02)中有 5 个请求。 如果请求到达 00:01:15，即窗口[00:01, 00:02)的 25％位置，我们按公式计算请求数：9 x（1 - 25％）+ 5 = 14.75 > 10.因此，我们拒绝这一要求。 即使两个窗口都没有超过限制，请求也会被拒绝，因为前一窗口和当前窗口的加权总和确实超过了限制。
- 这仍然不准确，因为它假设前一窗口中的请求分布是偶数，这可能不是真的。 但是比较固定窗口计数器，它只保证每个窗口内的速率，而滑动窗口日志，具有巨大的内存占用，滑动窗口更实用。

## 漏桶算法

![Image text](https://raw.githubusercontent.com/laniakea001/java-day-learn/master/src/main/resources/static/readMeImage/漏桶算法.webp)
一个系统处理请求，就像一个固定容量的水桶去溜进来的水，同时也让水流出去，但是它无法预见有多少水流进来和水流进来的速度，它只能够控制从桶底水流出去的速度，多出来的水，就只好让它从桶边流出去了。这个从桶底流出去的水就是系统正常处理的请求，从旁边流出去的水就是系统拒绝掉的请求。如此一来，我们只要监控系统单位时间内处理请求的速率就可以了，速率超过上限后的请求都给拒绝掉就可以了。

## 令牌桶算法

![Image text](https://raw.githubusercontent.com/laniakea001/java-day-learn/master/src/main/resources/static/readMeImage/令牌桶算法.webp)

- 令牌桶即是以一定速率生成 token 并放入桶中，请求进入系统需要先拿到 token 才能进行业务处理，无 token 的请求则拒绝掉。令牌桶算法实际上跟漏桶算法很相似，而实际使用中其实也不需要另起线程生成 token，只需要把握好 token 生成速率和当前应该剩余的 token 数量即可。

- 在时间点刷新的临界点上，只要剩余 token 足够，令牌桶算法会允许对应数量的请求通过，而后刷新时间因为 token 不足，流量也会被限制在外，这样就比较好的控制了瞬时流量。因此，令牌桶算法也被广泛使用。
