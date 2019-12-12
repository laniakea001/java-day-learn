# day 16 实现一致性 hash

- 一致性 hash 算法通过一个叫作一致性 hash 环的数据结构实现。这个环的起点是 0，终点是 2^32 - 1，并且起点与终点连接，环的中间的整数按逆时针分布，故这个环的整数分布范围是[0, 2^32-1]
- 将对象和机器都放置到同一个 hash 环后，在 hash 环上顺时针查找距离这个对象的 hash 值最近的机器，即是这个对象所属的机器。
- 使用简单的求模方法，当新添加机器后会导致大部分缓存失效的情况，使用一致性 hash 算法后这种情况则会得到大大的改善。

## 进一步地

- 无法解决不均衡的问题
- 将每台物理机器虚拟为一组虚拟机器，将虚拟机器放置到 hash 环上，如果需要确定对象的机器，先确定对象的虚拟机器，再由虚拟机器确定物理机器。
  ![Image text](https://raw.githubusercontent.com/laniakea001/java-day-learn/master/src/main/resources/static/readMeImage/一致性hash算法.jpg)

* [详细代码](https://github.com/laniakea001/java-day-learn/tree/master/src/main/java/com/hjj/daylearn/javadaylearn/day16_hash)
