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

# day 13 GC CMS，CMS 的参数

```java
-server                                             ## 服务器模式
-Xms2g                                              ## 初始化堆内存大小
-Xmx2g                                              ## 堆内存最大值
-Xmn256m                                            ## 年轻代内存大小，整个JVM内存=年轻代 + 年老代 + 持久代
-Xss256k                                            ## 设置每个线程的堆栈大小
-XX:PermSize=256m                                   ## 持久代内存大小
-XX:MaxPermSize=256m                                ## 最大持久代内存大小
-XX:ReservedCodeCacheSize=256m                      ## 代码缓存，存储已编译方法生成的本地代码
-XX:+UseCodeCacheFlushing                           ## 代码缓存满时，让JVM放弃一些编译代码
-XX:+DisableExplicitGC                              ## 忽略手动调用GC, System.gc()的调用就会变成一个空调用，完全不触发GC
-Xnoclassgc                                         ## 禁用类的垃圾回收，性能会高一点
-XX:+UseConcMarkSweepGC                             ## 并发标记清除（CMS）收集器
-XX:+CMSParallelRemarkEnabled                       ## 启用并行标记，降低标记停顿
-XX:+UseParNewGC                                    ## 对年轻代采用多线程并行回收，这样收得快
-XX:+UseCMSCompactAtFullCollection                  ## 在FULL GC的时候对年老代的压缩，Full GC后会进行内存碎片整理，过程无法并发，空间碎片问题没有了，但提顿时间不得不变长了
-XX:CMSFullGCsBeforeCompaction=3                    ## 多少次Full GC 后压缩old generation一次
-XX:LargePageSizeInBytes=128m                       ## 内存页的大小
-XX:+UseFastAccessorMethods                         ## 原始类型的快速优化
-XX:+UseCMSInitiatingOccupancyOnly                  ## 使用设定的回收阈值(下面指定的70%)开始CMS收集,如果不指定,JVM仅在第一次使用设定值,后续则自动调整
-XX:CMSInitiatingOccupancyFraction=70               ## 使用cms作为垃圾回收使用70％后开始CMS收集
-XX:SoftRefLRUPolicyMSPerMB=50                      ## Soft reference清除频率，默认存活1s,设置为0就是不用就清除
-XX:+AlwaysPreTouch                                 ## 强制操作系统把内存真正分配给JVM
-XX:+PrintClassHistogram                            ## 按下Ctrl+Break后，打印类的信息
-XX:+PrintGCDetails                                 ## 输出GC详细日志
-XX:+PrintGCTimeStamps                              ## 输出GC的时间戳（以基准时间的形式）
-XX:+PrintHeapAtGC                                  ## 在进行GC的前后打印出堆的信息
-XX:+PrintGCApplicationConcurrentTime               ## 输出GC之间运行了多少时间
-XX:+PrintTenuringDistribution                      ## 参数观察各个Age的对象总大小
-XX:+PrintGCApplicationStoppedTime                  ## GC造成应用暂停的时间
-Xloggc:../log/gc.log                               ## 指定GC日志文件的输出路径
-ea                                                 ## 打开断言机制，jvm默认关闭
-Dsun.io.useCanonCaches=false                       ## java_home没有配置，或配置错误会报异常
-Dsun.awt.keepWorkingSetOnMinimize=true             ## 可以让IDEA最小化到任务栏时依然保持以占有的内存，当你重新回到IDEA，能够被快速显示，而不是由灰白的界面逐渐显现整个界面，加快回复到原界面的速度
-Djava.net.preferIPv4Stack=true                     ## 让tomcat默认使用IPv4
-Djdk.http.auth.tunneling.disabledSchemes=""        ## 等于Basic会禁止proxy使用用户名密码这种鉴权方式,反之空就可以使用
-Djsse.enablesSNIExtension=false                    ## SNI支持，默认开启，开启会造成ssl握手警告
-XX:+HeapDumpOnOutOfMemoryError                     ## 表示当JVM发生OOM时，自动生成DUMP文件
-XX:HeapDumpPath=D:/data/log                        ## 表示生成DUMP文件的路径，也可以指定文件名称，如果不指定文件名，默认为：java_<pid>_<date>_<time>_heapDump.hprof。
-XX:-OmitStackTraceInFastThrow                      ## 省略异常栈信息从而快速抛出,这个配置抛出这个异常非常快，不用额外分配内存，也不用爬栈,但是出问题看不到stack trace，不利于排查问题
-Dfile.encoding=UTF-8                               ## 设置文件编码
-Duser.name=hjj                                     ## 设置用户名
NewRatio：3                                         ## 新生代与年老代的比例。比如为3，则新生代占堆的1/4，年老代占3/4。
SurvivorRatio：8                                    ## 新生代中调整eden区与survivor区的比例，默认为8，即eden区为80%的大小，两个survivor分别为10%的大小。
PretenureSizeThreshold：10m                         ## 晋升年老代的对象大小。默认为0，比如设为10M，则超过10M的对象将不在eden区分配，而直接进入年老代。
MaxTenuringThreshold：15                            ## 晋升老年代的最大年龄。默认为15，比如设为10，则对象在10次普通GC后将会被放入年老代。
-XX:MaxTenuringThreshold=0                          ## 垃圾最大年龄，如果设置为0的话,则年轻代对象不经过Survivor区,直接进入年老代，该参数只有在串行GC时才有效
-XX:+HeapDumpBeforeFullGC                           ## 当JVM 执行 FullGC 前执行 dump
-XX:+HeapDumpAfterFullGC                            ## 当JVM 执行 FullGC 后执行 dump
-XX:+HeapDumpOnCtrlBreak                            ## 交互式获取dump。在控制台按下快捷键Ctrl + Break时，JVM就会转存一下堆快照
-XX:+PrintGC                                        ## 输出GC日志
-verbose:gc                                         ## 同PrintGC,输出GC日志
-XX:+PrintGCDateStamps                              ## 输出GC的时间戳（以日期的形式，如 2013-05-04T21:53:59.234+0800）
-XX:+PrintFlagsInitial                              ## 显示所有可设置参数及默认值
-enablesystemassertions                             ## 激活系统类的断言
-esa                                                ## 同上
-disablesystemassertions                            ## 关闭系统类的断言
-dsa                                                ## 同上
-XX:+ScavengeBeforeFullGC                           ## FullGC前回收年轻代内存，默认开启
-XX:+CMSScavengeBeforeRemark                        ## CMS remark前回收年轻代内存
-XX:+CMSIncrementalMode                             ## 采用增量式的标记方式，减少标记时应用停顿时间
-XX:+CMSClassUnloadingEnabled                       ## 相对于并行收集器，CMS收集器默认不会对永久代进行垃圾回收。如果希望回收，就使用该标志，注意，即使没有设置这个标志，一旦永久代耗尽空间也会尝试进行垃圾回收，但是收集不会是并行的，而再一次进行Full GC
-XX:+CMSConcurrentMTEnabled                         ## 当该标志被启用时，并发的CMS阶段将以多线程执行(因此，多个GC线程会与所有的应用程序线程并行工作)。该标志已经默认开启，如果顺序执行更好，这取决于所使用的硬件，多线程执行可以通过-XX：-CMSConcurremntMTEnabled禁用。
```

## CMS 重要参数：

- CMSInitiatingOccupancyFraction：触发 CMS 收集器的内存比例。比如 60%的意思就是说，当内存达到 60%，就会开始进行 CMS 并发收集。
- UseCMSCompactAtFullCollection：这个前面已经提过，用于在每一次 CMS 收集器清理垃圾后送一次内存整理。
- CMSFullGCsBeforeCompaction：设置在几次 CMS 垃圾收集后，触发一次内存整理。

# day 14 JVM 的一些排查工具 jstat, jstack ,jmap

- [参考](https://my.oschina.net/feichexia/blog/196575)

# day 15 栈的最大值问题

给定一组数，求最大值，利用栈结构，时间复杂度为 O(1)

- [详细代码](https://github.com/laniakea001/java-day-learn/tree/master/src/main/java/com/hjj/daylearn/javadaylearn/day15_stackMax)
