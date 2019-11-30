# day 06 数据库的集群

## 方案1: 主从（Repliaction 集群方案）
### 特点
* 速度快
* 弱一直性
* 低价值
### 使用场景
* 日志
* 新闻
* 帖子


## 方案2: PXC 集群方案（ Percona XtraDB Cluster ）
### 特点
* 速度慢
* 强一致性
* 高价值
### 使用场景
* 订单
* 账户
* 财务

## PXC方案 和 Replication方案对比 
### 读写
![Image text](https://raw.githubusercontent.com/laniakea001/java-day-learn/master/src/main/resources/static/readMeImage/pxc方案1.png)
* PXC在任何节点的写入都会同步到其他节点，数据是双向同步的（在任何节点都可以同时读写）
* Replication方案是在master数据库进行写操作，在slave数据库进行读操纵，数据同步是单向的；
### 事务（数据一致性）
![Image text](https://raw.githubusercontent.com/laniakea001/java-day-learn/master/src/main/resources/static/readMeImage/pxc方案2.png)
![Image text](https://raw.githubusercontent.com/laniakea001/java-day-learn/master/src/main/resources/static/readMeImage/replication事务1.png)
* PXC具有强一致性。当一个请求发送到PXC集群中的一个数据库（NODE1）时，会将请求同步到其他的数据库，待其他的节点都成功的提交事务后，才将写入成功的响应返回；
* Replication具有弱一致性。写入请求到达master数据库时，执行成功后，返回响应。同时，异步地将写入的数据同步到从数据库；如果从数据库写入失败，但是客户端已经收到成功的响应，这是弱一致性的体现；
## 搭建方案
* [Replication](https://my.oschina.net/liuyuantao/blog/1860806)
* [PXC](https://blog.csdn.net/qq_33466466/article/details/84670368)
# day 07 消息可达性和唯一消费
## 大前提：mq的高可用
+ [如何保证消息队列的高可用](https://blog.csdn.net/u014801403/article/details/80312677)
## 消息可达性
### 消息投递的核心流程
![Image text](https://raw.githubusercontent.com/laniakea001/java-day-learn/master/src/main/resources/static/readMeImage/mq-canReceive.png)
+ 上半场 mq-client-sender --> mq-server
1. mq-client将消息发送给mq-server（调用api：sendMsg）
2. mq-server将消息落地，即为发送成功；
3. mq-server将应答发送给mq-client（调用api：sendCallBack）
+ 下半场 mq-server --> mq-client-receive
4. mq-server将消息发送给mq-client（调用api：recvCallBack）
5. mq-client回复应答（调用api：sendAck）
6. mq-server收到ack，将之前落地的消息删除，完成消息的可靠投递
### 消息何时丢失
+ 上述6个环节，都有可能丢失
### 如何保证消息的可达性--最终投递
#### 上半场的超时和重传
+ mq-client-sender发送失败，内置的timer会自动重发，直到期望的3；
+ 如果超过N次仍未收到，则sendCallBack回调发送失败；
+ 此过程中，mq-server可能会收到同一条消息的多次重发；
#### 下半场的超时和重传
+ mq-server发送失败，内置的timer会自动重发，直到得到5且6成功；
+ 这个过程中，可能会重发多次消息；
+ 一般采用指数退避的策略：先隔x秒重发，然后2x秒重发，4x秒重发，以此类推；
## 消息唯一消费（消息的幂等性）
### 上半场
+ 重发消息的接收方是mq-server；
+ 与业务无关，与mq有关
+ mq内部生成唯一的inner-msg-id，作为去重和幂等的依据
#### 特点
1. 全局唯一
2. mq生成，业务无关性，对消息发送方和消息接收方屏蔽
### 下半场
+ 重发消息的接收方是mq-client-seceive；
+ 与mq无关，也业务有关；
+ 由消息的接收方负责判重，负责幂等；
+ 业务消息体中，必须有个biz-id，作为判重和幂等的依据；
#### 特点
1. 单业务唯一；
2. 业务相关，mq透明；
# day 08 redis内存不足，如何解决
## 淘汰策略（截流）
+ 当redis的实际内存超过maxmemory时，需要采取memory-policy
+ 支持的规则

|规则名称|规则说明|
|---|---|
|volatile-lru|使用LRU算法删除一个键（只对设置了生存时间的键）|
|allkeys-lru|使用LRU算法删除一个键|
|volatile-random|随机删除一个键（只对设置了生存时间的键）|
|allkeys-random|随机删除一个键|
|volatile-ttl|删除生存时间最近的一个键|
|noeviction（默认）| 	不删除键，只返回错误|
## 集群（开源）
redis仅支持单实例，内存一般是10-20G，对于更大的缓存需求（100-200G），需要通过集群来支持；
### 集群的3种实现方式
#### 客户端分片
通过业务代码，自己实现分片--代码逻辑控制
##### 优势
+ 可以自己控制分片算法，性能较好
##### 劣势
+ 维护成本高，扩容/缩容等运维操作都需要单独实现
#### 代理分片
代理程序接收来自业务程序的请求，根据路由规则，将请求分发给正确的redis实例，并返回给业务程序。类似，Twemproxy、Codis中间件实现；
##### 优势
+ 运维方便，程序不需要关心如何链接redis；
##### 劣势
+ 20%的新能消耗
+ 无法平滑扩容/缩容（codis通过预分片，达到Auto Rebalance）
+ 需要执行脚本迁移数据，不方便
#### RedisCluster
官方的集群方案
##### 优势
+ 官方出品，无中心点，和客户端直连，性能好；
##### 劣势
+ 方案太重
+ 无法平滑扩容/缩容
+ 需要执行相应的脚本，不方便
# day 09 mysql索引的使用和原理
## 什么是索引？

正确的创建合适的索引是提升数据库查询性能的基础。

索引是为了加速对表中数据行的检索而创建的一种分散存储的数据结构。

## 为什么要用索引？

- 索引能极大的减少存储引擎需要扫描的数据量
- 索引可以把随机IO变成顺序IO
- 所以可以帮助我们在进行分组、排序等操作时，避免使用临时表

## 为什么是B+Tree

### 二叉查找树

- 最差情况可能形成链表结构

### 平衡二叉查找树

- 太深，数据处的深度决定了IO操作的次数，IO操作耗时大
- 太小，每一个磁盘块（节点/页）保存的数据量太小 ，没有很好的利用操作磁盘IO的数据交换特性，也没有利用好磁盘IO的预读能力（空间局部性原理），从而带来频繁的IO操作

### 多路平衡查找树B-Tree

- 数据在节点中，按页IO时查找的数据量会被非关键数据占用
- 查询速度不稳定，可能查找第一层数据之后就返回结果，也可能查找很多层数据之后返回

### 加强版多路平衡查找树B+Tree

- 只有叶子节点保存数据，所有查找都必须查找到叶子节点。
- 非叶子节点只存索引，一次按页IO的数据量会更大。

### B+Tree与B-Tree的区别

1. B+节点关键字搜索采用闭合区间
2. B+非叶节点不保存数据相关信息，只保存关键字和子节点的引用
3. B+关键字对应的数据保存在叶子节点中
4. B+叶子节点是顺序排列的，并且相邻节点剧有顺序引用的关系

### 为什么选用B+Tree?

- B+树是B-树的变种,多路绝对平衡查找树，他拥有B-树的优势
- B+树扫库、表能力更强
- B+树的磁盘读写能力更强
- B+树的排序能力更强
- B+树的查询效率更加稳定

## Mysql中B+Tree索引的体现形式

### myisam引擎

- .myi文件存索引,.myd文件存数据
- 不同列上的关键字最后都得到的是一个指向.myd文件的地址

### innodb引擎

- 以主键为索引来组织数据的存储
- 辅助索引得到主键索引的id
  聚集索引-数据库表行中数据的物理顺序与键值的逻辑（索引）顺序相同

## 索引的应用

### 列的离散性count(distinct col):count(col)

离散型越高选择性就月好

### 最左匹配原则

对索引关键字进行计算（对比），一定是从左往右一次进行，且不可跳过

### 联合索引

**单列索引**
 节点中关键字[name]

**联合索引**
 节点中关键字[name,phone]

**单列索引是特殊的联合索引**

**联合索引列选择原则**

1. 经常用的列优先[最左匹配原则]
2. 选择性（离散度）高的列优先[离散度高原则]
3. 宽度小的列优先[最小空间原则]

### 覆盖索引

如果查询列可通过索引节点中的关键字直接返回，则该索引称之为覆盖索引。

覆盖索引可减少数据库IO，将随机IO变为顺序IO，可提高查询性能。

### 总结

- 索引列的数据长度能少则少
- 索引一定不是越多越好，越全越好，一定是建合适的
- 匹配列浅醉可用到索引，like %9999%、like %9999%用不到索引，like 9999%在列离散度高的时候能用到索引，离散度低的时候用不到
- where条件中not in和<>操作无法使用索引
- 匹配范围值，order by也可用到索引
- 多用指定列查询，只返回自己想要的数据列，少用select *;
- 联合索引中如果不是按照索引最左列开始查找，无法使用索引
- 联合索引中精确匹配最左前列并范围匹配另外一列可以用到索引
- 联合索引中如果查询中有某个列的范围查询，则其右边所有列都无法使用索引
