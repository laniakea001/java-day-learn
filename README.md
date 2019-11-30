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
# day 07 消息可达性和唯一消费

# day 08 redis内存不足，如何解决

# day 09 mysql索引的使用和原理