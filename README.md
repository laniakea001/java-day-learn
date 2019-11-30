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

# day 09 mysql索引的使用和原理