# Redis实现文章投票功能



## 需求

1.能够按照时间分页倒叙查看文章信息
2.能够给文章投票，一个用户给一篇文章只能投票一次
3.需要记录分值。每次投票后就+10分

 

## 思路

1.设计一个Hash，用于存储文章的基本信息（标题、内容、创建人）。键：article:1
2.设计一个Set，用于记录一篇文章被哪些用户投过票。键：vole:1
3.设计一个ZSet，用于记录每一篇文章的分值。键：score



## 代码

### pom

```xml
		<dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
        </dependency>
```

### 工具类

```java
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


//设置redis连接池
public class RedisUtil {
    private static JedisPool pool;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(100);
        config.setTestOnBorrow(true);

        pool = new JedisPool(config, "192.168.5.55", 6379);
    }

    private RedisUtil() {
    }
		
    //获得连接
    public static Jedis getJedisClient() {
        return pool.getResource();
    }
	
    //关闭连接
    public static void dispose(Jedis jedis) {
        pool.returnResourceObject(jedis);
    }
}
```

### 测试

```java
import demo.vote.RedisUtil;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class TestVote {

    /**
     * 发表文章
     * 逻辑：
     * 1.文章编号采用incr方法自增长
     * 2.定义Redis的Hash类型存放文章信息，键采用 "article:"+文章编号的方式
     * 3.定义Set用于记录哪些用户给这篇文章投过票，键采用 "vole:"+文章编号的方式
     * 4.定义ZSet用于记录每篇文章的分值，方便排序，键使用"score"
     */
    @Test
    public void addArticle() {
        Jedis jedis = RedisUtil.getJedisClient();
        // 定义发布文章：编号、标题、内容、创建人 Hash
        HashMap<String, String> map = new HashMap<String, String>();
        long id = jedis.incr("article:");// 文章id，自增长
        map.put("title", "文章标题->" + id);
        map.put("content", "文章内容->" + id);
        map.put("user_id", String.valueOf(userId));
        map.put("create_time", getDate());
        jedis.hmset("article:" + id, map);

        // 自己给自己投上一票 Set
        jedis.sadd("vole:" + id, userId + "");
        // 给自己+10分 ZSet
        jedis.zadd("score", 10, id + "");

        printArticle(jedis, id);

        RedisUtil.dispose(jedis);
    }

    /**
     * 给文章投票
     * 逻辑：
     * 1.记录文章投票用户集合的Set新增一条数据sadd，如果返回1表示新增成功，返回0表示已存在
     * 2.记录分值的ZSet调用zincrby方法加10分
     */

    int userId = 300;//用户id
    int articleId = 2;

    @Test
    public void voteArticle() {
        Jedis jedis = RedisUtil.getJedisClient();
        try {
            // 查找文章
            if (!jedis.exists("article:" + articleId)) {
                System.out.println("文章:" + articleId + " 不存在.");
                return;
            }
            // 用户投票
            if (jedis.sadd("vole:" + articleId, userId + "") == 0) {
                System.out.println("用户：" + userId + " 已对文章:" + articleId + " 进行过投票，不可重复投票");
                return;
            }
            // +10分
            jedis.zincrby("score", 10, articleId + "");
            printArticle(jedis, articleId);
        } finally {
            RedisUtil.dispose(jedis);
        }
    }

    /**
     * 按分值从高到低排序
     * 1.调用ZSet的zrevrange方法根据分值倒序，获得一个集合
     * 2.循环集合去Hash中查询文章信息
     */
    @Test
    public void allArticle() {
        Jedis jedis = RedisUtil.getJedisClient();
        try {
            Set<String> set = jedis.zrevrange("score", 0, 1000);// 返回的是文章编号
            for (String aId : set) {
                System.out.println("<<=================================================>>");
                printArticle(jedis, Integer.parseInt(aId));
            }
        } finally {
            RedisUtil.dispose(jedis);
        }
    }

    /**
     * 打印文章信息,参数是文章的id
     */
    private void printArticle(Jedis jedis, long id) {
        /* 打印日志 */
        System.out.println("***********************");
        List<String> list = jedis.hmget("article:" + id, "title", "content", "user_id", "create_time");
        System.out.println("文章编号:" + id);
        System.out.println("文章标题:" + list.get(0));
        System.out.println("文章内容:" + list.get(1));
        System.out.println("发布用户:" + list.get(2));
        System.out.println("发布时间:" + list.get(3));
        System.out.println("***********************");
        double scores = jedis.zscore("score", id + "");
        System.out.println("文章分值:" + scores);

        Set<String> set = jedis.smembers("vole:" + id);
        System.out.println("给文章投票的用户:" + String.join(",", set));
    }

    /**
     * 获取当前时间
     */
    private String getDate() {
        java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = format1.format(new Date());
        return s;
    }
}
```

