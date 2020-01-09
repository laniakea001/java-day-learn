# day 26 LRU 的实现

如何来设计一款 LRU 算法呢？对于这种类似序列的结构我们一般可以选择链表或者是数组来构建。

差异对比：

1. 数组 查询比较快，但是对于增删来说是一个不是一个好的选择
2. 链表 查询比较慢，但是对于增删来说十分方便 O(1)时间复杂度内搞定

有没有办法既能够让其搜索快，又能够快速进行增删操作。
我们可以选择链表+hash 表，hash 表的搜索可以达到 0(1)时间复杂度，这样就完美的解决我们搜索时间慢的问题了

- Hash 表，在 Java 中 HashMap 是我们的不二选择
- 链表，Node 一个双向链表的实现，Node 中存放的是数结构如下

## 大致思路:

1. 构建双向链表节点 ListNode，应包含 key,value,prev,next 这几个基本属性

2. 对于 Cache 对象来说，我们需要规定缓存的容量，所以在初始化时，设置容量大小，然后实例化双向链表的 head,tail，并让 head.next->tail tail.prev->head，这样我们的双向链表构建完成

3. 对于 get 操作,我们首先查阅 hashmap，如果存在的话，直接将 Node 从当前位置移除，然后插入到链表的首部，在链表中实现删除直接让 node 的前驱节点指向后继节点，很方便.如果不存在，那么直接返回 Null

4. 对于 put 操作，先判断是否超出容量，超出则删去尾部节点；其余操作同上述 3

- [详细代码](https://github.com/laniakea001/java-day-learn/tree/master/src/main/java/com/hjj/daylearn/javadaylearn/day26_LRU)
