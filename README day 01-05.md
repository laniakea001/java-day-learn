# java-day-learn

java-day-learn

# day 01 基础链表实现

# day 02 TCP/UDP

## TCP

### 三次握手，建立连接

- 客户端主动打开，发送连接请求报文段，将 SYN 标识位置为 1，Sequence Number 置为 x（TCP 规定 SYN=1 时不能携带数据，x 为随机产生的一个值），然后进入 SYN_SEND 状态
- 服务器收到 SYN 报文段进行确认，将 SYN 标识位置为 1，ACK 置为 1，Sequence Number 置为 y，Acknowledgment Number 置为 x+1，然后进入 SYN_RECV 状态，这个状态被称为半连接状态
- 客户端再进行一次确认，将 ACK 置为 1（此时不用 SYN），Sequence Number 置为 x+1，Acknowledgment Number 置为 y+1 发向服务器，最后客户端与服务器都进入 ESTABLISHED 状态

### 四次挥手，释放连接

- 客户端发送一个报文给服务端（没有数据），其中 FIN 设置为 1，Sequence Number 置为 u，客户端进入 FIN_WAIT_1 状态
- 服务端收到来自客户端的请求，发送一个 ACK 给客户端，Acknowledge 置为 u+1，同时发送 Sequence Number 为 v，服务端年进入 CLOSE_WAIT 状态
- 服务端发送一个 FIN 给客户端，ACK 置为 1，Sequence 置为 w，Acknowledge 置为 u+1，用来关闭服务端到客户端的数据传送，服务端进入 LAST_ACK 状态
- 客户端收到 FIN 后，进入 TIME_WAIT 状态，接着发送一个 ACK 给服务端，Acknowledge 置为 w+1，Sequence Number 置为 u+1，最后客户端和服务端都进入 CLOSED 状态

### Socket 的交互流程

- 服务器根据地址类型、socket 类型、以及协议来创建 socket。
- 服务器为 socket 绑定 IP 地址和端口号。
- 服务器 socket 监听端口号请求，随时准备接收客户端发来的连接，这时候服务器的 socket 并没有全部打开。
- 客户端创建 socket。
- 客户端打开 socket，根据服务器 IP 地址和端口号试图连接服务器 socket。
- 服务器 socket 接收到客户端 socket 请求，被动打开，开始接收客户端请求，知道客户端返回连接信息。这时候 socket 进入阻塞状态，阻塞是由于 accept() 方法会一直等到客户端返回连接信息后才返回，然后开始连接下一个客户端的连接请求。
- 客户端连接成功，向服务器发送连接状态信息。
- 服务器 accept() 方法返回，连接成功。
- 服务器和客户端通过网络 I/O 函数进行数据的传输。
- 客户端关闭 socket。
- 服务器关闭 socket。

### 实现一个简单 TCP 交互

https://github.com/laniakea001/java-day-learn/tree/master/src/main/java/com/hjj/daylearn/javadaylearn/day02_tcp_udp

## UDP

### UDP 和 TCP 的不同

- UDP：单个数据报，不用建立连接，简单，不可靠，会丢包，会乱序；
- TCP：流式，需要建立连接，复杂，可靠 ，有序。

# day 03 乐观锁/悲观锁

    都是处理高并发遇到的常见问题

## 乐观锁

    共享资源每次只给一个线程使用，其它线程阻塞，用完后再把资源转让给其它线程；
    如：
    + 版本号控制
    + CAS

## 悲观锁

    每次去拿数据的时候都认为别人不会修改，所以不会上锁，但是在更新的时候会判断一下在此期间别人有没有去更新这个数据；
    如：
    + synchronized
    + ReentrantLock

### 实现方法

#### 版本号机制

    一般是在数据表中加上一个数据版本号version字段，表示数据被修改的次数，当数据被修改时，version值会加一。当线程A要更新数据值时，在读取数据的同时也会读取version值，在提交更新时，若刚才读取到的version值为当前数据库中的version值相等时才更新，否则重试更新操作，直到更新成功。

#### CAS 算法,compare and swap（比较与交换）

    CAS算法涉及到三个操作数

        需要读写的内存值 V
        进行比较的值 A
        拟写入的新值 B

    当且仅当 V 的值等于 A时，CAS通过原子方式用新值B来更新V的值，否则不会执行任何操作（比较和替换是一个原子操作）。一般情况下是一个自旋操作，即不断的重试。

##### 存在问题

    ABA问题
    如果一个变量V初次读取的时候是A值，并且在准备赋值的时候检查到它仍然是A值，那我们就能说明它的值没有被其他线程修改过了吗？很明显是不能的，因为在这段时间它的值可能被改为其他值，然后又改回A，那CAS操作就会误认为它从来没有被修改过

## 2 种锁的比较

    - 乐观锁比较适合应用于读操作比较多的场景，减少阻塞；
    - 悲观锁比较适合应用于数据写操作多的场景，减少重试；

## 实现

https://github.com/laniakea001/java-day-learn/tree/master/src/main/java/com/hjj/daylearn/javadaylearn/day03_lock

# day 04 map 底层原理

## HashMap

### 介绍

- 它根据键的 HashCode 值存储数据,根据键可以直接获取它的值，具有很快的访问速度。
- HashMap 最多只允许一条记录的键为 Null(多条会覆盖);允许多条记录的值为 Null。
- 非同步的。

### 底层

数组+链表+红黑树
![Image text](https://raw.githubusercontent.com/laniakea001/java-day-learn/master/src/main/resources/static/readMeImage/hashmap.png)

## TreeMap

- 能够把它保存的记录根据键(key)排序,默认是按升序排序，也可以指定排序的比较器，
- 当用 Iterator 遍历 TreeMap 时，得到的记录是排过序的。
- TreeMap 不允许 key 的值为 null。非同步的。

### 底层

TreeMap 由 红黑树 实现

举个栗子 🌰：
基本树结构如下：
![Image text](https://raw.githubusercontent.com/laniakea001/java-day-learn/master/src/main/resources/static/readMeImage/树的基本结构.png)

插入 7，6，5，4，3

#### 二叉树举例

存储结果如图：
![Image text](https://raw.githubusercontent.com/laniakea001/java-day-learn/master/src/main/resources/static/readMeImage/二叉树存储.png)

#### 红黑树举例

存储结果如图：
![Image text](https://raw.githubusercontent.com/laniakea001/java-day-learn/master/src/main/resources/static/readMeImage/红黑树存储.png)

规则：

1. 节点是红色或黑色。

2. 根节点是黑色。

3. 每个叶子节点都是黑色的空节点（NIL 节点）。

4. 每个红色节点的两个子节点都是黑色。(从每个叶子到根的所有路径上不能有两个连续的红色节点)

5. 从任一节点到其每个叶子的所有路径都包含相同数目的黑色节点。

## Hashtable

- 与 HashMap 类似,不同的是:key 和 value 的值均不允许为 null
- 它支持线程的同步，即任一时刻只有一个线程能写 Hashtable，因此也导致了 Hashtale 在写入时会比较慢。

## LinkedHashMap

- 保存了记录的插入顺序，在用 Iterator 遍历 LinkedHashMap 时，先得到的记录肯定是先插入的.
- 在遍历的时候会比 HashMap 慢。key 和 value 均允许为空，非同步的。

# day 05 数据库的分库分表

## mysql 的分库分表

### 为什么要分表和分区？

日常开发中我们经常会遇到大表的情况，所谓的大表是指存储了百万级乃至千万级条记录的表。这样的表过于庞大，导致数据库在查询和插入的时候耗时太长，性能低下，如果涉及联合查询的情况，性能会更加糟糕。分表和表分区的目的就是减少数据库的负担，提高数据库的效率，通常点来讲就是提高表的增删改查效率。

### 什么是分表？

分表是将一个大表按照一定的规则分解成多张具有独立存储空间的实体表，我们可以称为子表，每个表都对应三个文件，MYD 数据文件，.MYI 索引文件，.frm 表结构文件。这些子表可以分布在同一块磁盘上，也可以在不同的机器上。app 读写的时候根据事先定义好的规则得到对应的子表名，然后去操作它。

### 什么是分区？

分区和分表相似，都是按照规则分解表。不同在于分表将大表分解为若干个独立的实体表，而分区是将数据分段划分在多个位置存放，可以是同一块磁盘也可以在不同的机器。分区后，表面上还是一张表，但数据散列到多个位置了。app 读写的时候操作的还是大表名字，db 自动去组织分区的数据。

### mysql 分表和分区有什么联系呢？

1. 都能提高 mysql 的性高，在高并发状态下都有一个良好的表现。
2. 分表和分区不矛盾，可以相互配合的，对于那些大访问量，并且表数据比较多的表，我们可以采取分表和分区结合的方式（如果 merge 这种分表方式，不能和分区配合的话，可以用其他的分表试），访问量不大，但是表数据很多的表，我们可以采取分区的方式等。
3. 分表技术是比较麻烦的，需要手动去创建子表，app 服务端读写时候需要计算子表名。采用 merge 好一些，但也要创建子表和配置子表间的 union 关系。
4. 表分区相对于分表，操作方便，不需要创建子表。

### 分表的几种方式：

1. mysql 集群
2. 自定义规则分表
3. 利用 merge 存储引擎来实现分表

### 分区的几种方式

1. Range
2. List
3. Hash
4. Key

## 分库分表要解决的问题

### 1、事务问题

解决事务问题目前有两种可行的方案：分布式事务和通过应用程序与数据库共同控制实现事务下面对两套方案进行一个简单的对比。

#### 方案一：使用分布式事务

优点：交由数据库管理，简单有效
缺点：性能代价高，特别是 shard 越来越多时

#### 方案二：由应用程序和数据库共同控制

原理：将一个跨多个数据库的分布式事务分拆成多个仅处 于单个数据库上面的小事务，并通过应用程序来总控 各个小事务。
优点：性能上有优势
缺点：需要应用程序在事务控制上做灵活设计。如果使用 了 spring 的事务管理，改动起来会面临一定的困难。

### 2、跨节点 Join 的问题

只要是进行切分，跨节点 Join 的问题是不可避免的。但是良好的设计和切分却可以减少此类情况的发生。解决这一问题的普遍做法是分两次查询实现。在第一次查询的结果集中找出关联数据的 id,根据这些 id 发起第二次请求得到关联数据。

### 3、跨节点的 count,order by,group by 以及聚合函数问题

这些是一类问题，因为它们都需要基于全部数据集合进行计算。多数的代理都不会自动处理合并工作。解决方案：与解决跨节点 join 问题的类似，分别在各个节点上得到结果后在应用程序端进行合并。和 join 不同的是每个结点的查询可以并行执行，因此很多时候它的速度要比单一大表快很多。但如果结果集很大，对应用程序内存的消耗是一个问题。

### 4、数据迁移，容量规划，扩容等问题

来自淘宝综合业务平台团队，它利用对 2 的倍数取余具有向前兼容的特性（如对 4 取余得 1 的数对 2 取余也是 1）来分配数据，避免了行级别的数据迁移，但是依然需要进行表级别的迁移，同时对扩容规模和分表数量都有限制。总得来说，这些方案都不是十分的理想，多多少少都存在一些缺点，这也从一个侧面反映出了 Sharding 扩容的难度。

### 5、事务

#### 分布式事务

参考： [关于分布式事务、两阶段提交、一阶段提交、Best Efforts 1PC 模式和事务补偿机制的研究](http://blog.csdn.net/bluishglc/article/details/7612811)

- 优点
  基于两阶段提交，最大限度地保证了跨数据库操作的“原子性”，是分布式系统下最严格的事务实现方式。
  实现简单，工作量小。由于多数应用服务器以及一些独立的分布式事务协调器做了大量的封装工作，使得项目中引入分布式事务的难度和工作量基本上可以忽略不计。
- 缺点
  系统“水平”伸缩的死敌。基于两阶段提交的分布式事务在提交事务时需要在多个节点之间进行协调,最大限度地推后了提交事务的时间点，客观上延长了事务的执行时间，这会导致事务在访问共享资源时发生冲突和死锁的概率增高，随着数据库节点的增多，这种趋势会越来越严重，从而成为系统在数据库层面上水平伸缩的"枷锁"， 这是很多 Sharding 系统不采用分布式事务的主要原因。

#### 基于 Best Efforts 1PC 模式的事务

参考 spring-data-neo4j 的实现。鉴于 Best Efforts 1PC 模式的性能优势，以及相对简单的实现方式，它被大多数的 sharding 框架和项目采用

#### 事务补偿（幂等值）

对于那些对性能要求很高，但对一致性要求并不高的系统，往往并不苛求系统的实时一致性，只要在一个允许的时间周期内达到最终一致性即可，这使得事务补偿机制成为一种可行的方案。事务补偿机制最初被提出是在“长事务”的处理中，但是对于分布式系统确保一致性也有很好的参考意义。笼统地讲，与事务在执行中发生错误后立即回滚的方式不同，事务补偿是一种事后检查并补救的措施，它只期望在一个容许时间周期内得到最终一致的结果就可以了。事务补偿的实现与系统业务紧密相关，并没有一种标准的处理方式。一些常见的实现方式有：对数据进行对帐检查;基于日志进行比对;定期同标准数据来源进行同步，等等。

### 6、ID 问题

一旦数据库被切分到多个物理结点上，我们将不能再依赖数据库自身的主键生成机制。一方面，某个分区数据库自生成的 ID 无法保证在全局上是唯一的；另一方面，应用程序在插入数据之前需要先获得 ID,以便进行 SQL 路由.
一些常见的主键生成策略

#### UUID

使用 UUID 作主键是最简单的方案，但是缺点也是非常明显的。由于 UUID 非常的长，除占用大量存储空间外，最主要的问题是在索引上，在建立索引和基于索引进行查询时都存在性能问题。

结合数据库维护一个 Sequence 表
此方案的思路也很简单，在数据库中建立一个 Sequence 表，表的结构类似于：

```sql
CREATE TABLE `SEQUENCE` (
    `table_name` varchar(18) NOT NULL,
    `nextid` bigint(20) NOT NULL,
    PRIMARY KEY (`table_name`)
) ENGINE=InnoDB
```

每当需要为某个表的新纪录生成 ID 时就从 Sequence 表中取出对应表的 nextid,并将 nextid 的值加 1 后更新到数据库中以备下次使用。此方案也较简单，但缺点同样明显：由于所有插入任何都需要访问该表，该表很容易成为系统性能瓶颈，同时它也存在单点问题，一旦该表数据库失效，整个应用程序将无法工作。有人提出使用 Master-Slave 进行主从同步，但这也只能解决单点问题，并不能解决读写比为 1:1 的访问压力问题。

#### Twitter 的分布式自增 ID 算法 Snowflake

在分布式系统中，需要生成全局 UID 的场合还是比较多的，twitter 的 snowflake 解决了这种需求，实现也还是很简单的，除去配置信息，核心代码就是毫秒级时间 41 位 机器 ID 10 位 毫秒内序列 12 位。

- 10---0000000000 0000000000 0000000000 0000000000 0 --- 00000 ---00000 ---000000000000
  在上面的字符串中，第一位为未使用（实际上也可作为 long 的符号位），接下来的 41 位为毫秒级时间，然后 5 位 datacenter 标识位，5 位机器 ID（并不算标识符，实际是为线程标识），然后 12 位该毫秒内的当前毫秒内的计数，加起来刚好 64 位，为一个 Long 型。

这样的好处是，整体上按照时间自增排序，并且整个分布式系统内不会产生 ID 碰撞（由 datacenter 和机器 ID 作区分），并且效率较高，经测试，snowflake 每秒能够产生 26 万 ID 左右，完全满足需要。

# day 06 数据库的集群

# day 07 消息可达性和唯一消费

# day 08 redis内存不足，如何解决

# day 09 mysql索引的使用和原理