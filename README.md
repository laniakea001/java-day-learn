# day 16 实现一致性 hash

- 一致性 hash 算法通过一个叫作一致性 hash 环的数据结构实现。这个环的起点是 0，终点是 2^32 - 1，并且起点与终点连接，环的中间的整数按逆时针分布，故这个环的整数分布范围是[0, 2^32-1]
- 将对象和机器都放置到同一个 hash 环后，在 hash 环上顺时针查找距离这个对象的 hash 值最近的机器，即是这个对象所属的机器。
- 使用简单的求模方法，当新添加机器后会导致大部分缓存失效的情况，使用一致性 hash 算法后这种情况则会得到大大的改善。

## 进一步地

- 无法解决不均衡的问题
- 将每台物理机器虚拟为一组虚拟机器，将虚拟机器放置到 hash 环上，如果需要确定对象的机器，先确定对象的虚拟机器，再由虚拟机器确定物理机器。
  ![Image text](https://raw.githubusercontent.com/laniakea001/java-day-learn/master/src/main/resources/static/readMeImage/一致性hash算法.jpg)

* [详细代码](https://github.com/laniakea001/java-day-learn/tree/master/src/main/java/com/hjj/daylearn/javadaylearn/day16_hash)

# day 17 微博限定用户每次输入最多 140 个字符，用户如果传字符串很长的链接，怎么办

## 方法 1：建立 hash 映射

- 计算长链接的 hash 值
- 缓存在 redis 中
- 处理冲突的 hash 值，参照 hashmap 的实现

## 方法 2： 分布式 id 生成器，定义唯一的分布式 id

### UUID

```java
//java 自带的 uuid
//使用简单、性能好、本地生成
String uuid = UUID.randomUUID().toString();
```

62 进制的 UUID 生成方法（java 版）

- [详细代码](https://github.com/laniakea001/java-day-learn/tree/master/src/main/java/com/hjj/daylearn/javadaylearn/day17_uuid)

### 数据库自增 id

可以使用两台数据库分别设置不同步长，生成不重复 ID 的策略来实现高可用

### 雪花算法

1. 1bit:一般是符号位，不做处理
2. 41bit:用来记录时间戳，这里可以记录 69 年，如果设置好起始时间比如今年是 2018 年，那么可以用到 2089 年，到时候怎么办？要是这个系统能用 69 年，我相信这个系统早都重构了好多次了。
3. 10bit:10bit 用来记录机器 ID，总共可以记录 1024 台机器，一般用前 5 位代表数据中心，后面 5 位是某个数据中心的机器 ID
4. 12bit:循环位，用来对同一个毫秒之内产生不同的 ID，12 位可以最多记录 4095 个，也就是在同一个机器同一毫秒最多记录 4095 个，多余的需要进行等待下毫秒。

- [详细代码](https://github.com/laniakea001/java-day-learn/tree/master/src/main/java/com/hjj/daylearn/javadaylearn/day17_uuid)

### 百度 UidGenerator

- [详细代码](https://github.com/baidu/uid-generator)

### Redis 生成 ID

Redis 的所有命令操作都是单线程的，本身提供像 incr 和 increby 这样的自增原子命令，所以能保证生成的 ID 肯定是唯一有序的
类似数据库自增 ID，性能优于数组库自增

# day 18 两个线程，一个只能存有数组 1、2、3 和另一个存有 a、b、c，然后通过调度，最终结果输出 1a2b3c

- [详细代码](https://github.com/laniakea001/java-day-learn/tree/master/src/main/java/com/hjj/daylearn/javadaylearn/day18_thread)
