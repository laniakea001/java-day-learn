# java-day-learn
java-day-learn

# day 01 基础链表实现
# day 02 TCP/UDP
## TCP
### 三次握手，建立连接

 + 客户端主动打开，发送连接请求报文段，将SYN标识位置为1，Sequence Number置为x（TCP规定SYN=1时不能携带数据，x为随机产生的一个值），然后进入SYN_SEND状态
 + 服务器收到SYN报文段进行确认，将SYN标识位置为1，ACK置为1，Sequence Number置为y，Acknowledgment Number置为x+1，然后进入SYN_RECV状态，这个状态被称为半连接状态
 + 客户端再进行一次确认，将ACK置为1（此时不用SYN），Sequence Number置为x+1，Acknowledgment Number置为y+1发向服务器，最后客户端与服务器都进入ESTABLISHED状态

### 四次挥手，释放连接

+ 客户端发送一个报文给服务端（没有数据），其中FIN设置为1，Sequence Number置为u，客户端进入FIN_WAIT_1状态
+ 服务端收到来自客户端的请求，发送一个ACK给客户端，Acknowledge置为u+1，同时发送Sequence Number为v，服务端年进入CLOSE_WAIT状态
+ 服务端发送一个FIN给客户端，ACK置为1，Sequence置为w，Acknowledge置为u+1，用来关闭服务端到客户端的数据传送，服务端进入LAST_ACK状态
+ 客户端收到FIN后，进入TIME_WAIT状态，接着发送一个ACK给服务端，Acknowledge置为w+1，Sequence Number置为u+1，最后客户端和服务端都进入CLOSED状态

### Socket 的交互流程

+ 服务器根据地址类型、socket 类型、以及协议来创建 socket。
+ 服务器为 socket 绑定 IP 地址和端口号。
+ 服务器 socket 监听端口号请求，随时准备接收客户端发来的连接，这时候服务器的 socket 并没有全部打开。
+ 客户端创建 socket。
+ 客户端打开 socket，根据服务器 IP 地址和端口号试图连接服务器 socket。
+ 服务器 socket 接收到客户端 socket 请求，被动打开，开始接收客户端请求，知道客户端返回连接信息。这时候 socket 进入阻塞状态，阻塞是由于 accept() 方法会一直等到客户端返回连接信息后才返回，然后开始连接下一个客户端的连接请求。
+ 客户端连接成功，向服务器发送连接状态信息。
+ 服务器 accept() 方法返回，连接成功。
+ 服务器和客户端通过网络 I/O 函数进行数据的传输。
+ 客户端关闭 socket。
+ 服务器关闭 socket。

### 实现一个简单 TCP 交互
https://github.com/laniakea001/java-day-learn/tree/master/src/main/java/com/hjj/daylearn/javadaylearn/day02_tcp_udp
## UDP
### UDP 和 TCP 的不同
+ UDP：单个数据报，不用建立连接，简单，不可靠，会丢包，会乱序；
+ TCP：流式，需要建立连接，复杂，可靠 ，有序。


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
#### CAS算法,compare and swap（比较与交换）
    CAS算法涉及到三个操作数

        需要读写的内存值 V
        进行比较的值 A
        拟写入的新值 B

    当且仅当 V 的值等于 A时，CAS通过原子方式用新值B来更新V的值，否则不会执行任何操作（比较和替换是一个原子操作）。一般情况下是一个自旋操作，即不断的重试。
##### 存在问题
    ABA问题
    如果一个变量V初次读取的时候是A值，并且在准备赋值的时候检查到它仍然是A值，那我们就能说明它的值没有被其他线程修改过了吗？很明显是不能的，因为在这段时间它的值可能被改为其他值，然后又改回A，那CAS操作就会误认为它从来没有被修改过  
## 2种锁的比较
    - 乐观锁比较适合应用于读操作比较多的场景，减少阻塞；
    - 悲观锁比较适合应用于数据写操作多的场景，减少重试；
## 实现
https://github.com/laniakea001/java-day-learn/tree/master/src/main/java/com/hjj/daylearn/javadaylearn/day03_lock

