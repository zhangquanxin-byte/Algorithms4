## redisTemplate和stringRedisTemplate对比

RedisTemplate看这个类的名字后缀是Template，如果了解过Spring如何连接关系型数据库的，大概不会难猜出这个类是做什么的 ，它跟JdbcTemplate一样封装了对Redis的一些常用的操作，当然StringRedisTemplate跟RedisTemplate功能类似那么肯定就会有人问，为什么会需要两个Template呢，一个不就够了吗？其实他们两者之间的区别主要在于他们使用的序列化类。

```
RedisTemplate使用的是 JdkSerializationRedisSerializer 序列化对象
StringRedisTemplate使用的是 StringRedisSerializer 序列化String
```



### 1、StringRedisTemplate

- 主要用来存储字符串，StringRedisSerializer的泛型指定的是String。当存入对象时，会报错 ：can not cast into String。

- 可见性强，更易维护。如果过都是字符串存储可考虑用StringRedisTemplate。

  

### 2、RedisTemplate

- 可以用来存储对象，但是要实现Serializable接口。
- 以二进制数组方式存储，内容没有可读性。



### 3、区别

 两者的关系是StringRedisTemplate继承RedisTemplate。

 两者的数据是不共通的；也就是说StringRedisTemplate只能管理StringRedisTemplate里面的数据，RedisTemplate只能管理RedisTemplate中的数据。



stringRedisTemplate:详细用法：易学

https://www.e-learn.cn/content/redis/1744950