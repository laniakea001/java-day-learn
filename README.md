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

## UDP
### UDP 和 TCP 的不同
+ UDP：单个数据报，不用建立连接，简单，不可靠，会丢包，会乱序；
+ TCP：流式，需要建立连接，复杂，可靠 ，有序。
