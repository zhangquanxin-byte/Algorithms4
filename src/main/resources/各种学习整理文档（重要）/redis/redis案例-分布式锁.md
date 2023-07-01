分布式锁一般有三种实现方式：1. 数据库乐观锁；2. 基于Redis的分布式锁；3. 基于ZooKeeper的分布式锁。

本篇介绍基于Redis实现分布式锁。

- set命令要用 set key value px milliseconds nx；保证原子性
- value要具有唯一性，释放锁时要验证value值，不能误解锁
- 解锁要使用lua脚本，也是为了保证原子性

```xml
		<dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>2.9.3</version>
        </dependency>
```



```java
public class OrderIdGenerator {

    private static int count;

    public String get() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd-HH-mm-ss");
        return sdf.format(new Date()) + "-" + (++count);
    }

}
```



```java
public interface Lock {
    void lock();

    void unlock();
}
```



```java

public abstract class AbstractLock implements Lock {

    @Override
    public void lock() {
        if (tryLock()) {
            System.out.println(Thread.currentThread().getName() + "\t:获得锁");
        } else {
            //等待
            waitLock();
            //等待结束,继续抢锁,羊群效应
            lock();
        }
    }

    public abstract boolean tryLock();

    public abstract void waitLock();
}

```



```java

public class RedisUtils {
    private static JedisPool pool;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(100);
        config.setTestOnBorrow(true);

        pool = new JedisPool(config, "192.168.5.55", 6379);
    }

    private RedisUtils() {
    }

    public static Jedis getJedisClient() {
        return pool.getResource();
    }

    public static void dispose(Jedis jedis) {
        pool.returnResourceObject(jedis);
    }
}

```



```java

public class RedisLock extends AbstractLock {

    private static final String LOCK_SUCCESS = "OK";

    private boolean lock = false;
    private String key = "lock";
    private String value = UUID.randomUUID().toString();
    private long timeout = 5000; // ms
    private int expireTime = 2000; // ms
    private int waitTime = 1000; // ms

    private Jedis client = RedisUtils.getJedisClient();

    @Override
    public boolean tryLock() {
        long currentTime = System.currentTimeMillis();
        try {
            //在timeout的时间范围内不断轮询锁
            while (System.currentTimeMillis() - currentTime < timeout) {
                //锁不存在的话，设置锁并设置锁过期时间，即加锁
                //set(String key, String value, String nxxx, String expx, int time)
                String result = client.set(key, value, "NX", "PX", expireTime);
                if (LOCK_SUCCESS.equals(result)) {
                    this.lock = true;
                    return true;
                }
                //
                System.out.println(Thread.currentThread().getName() + "等待锁");
                //短暂休眠
                Thread.sleep(waitTime);
            }
        } catch (Exception e) {
            throw new RuntimeException("locking error", e);
        }
        return false;
    }

    @Override
    public void waitLock() {
        System.out.println(Thread.currentThread().getName() + "等待锁====超时");
    }

    @Override
    public void unlock() {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        long result = (Long) client.eval(script, Collections.singletonList(key), Collections.singletonList(value));
        if (result == 1L) {
            System.out.println(Thread.currentThread().getName() + "释放锁: ok");
        } else {
            System.out.println(Thread.currentThread().getName() + "释放锁: error");
        }
        RedisUtils.dispose(client);

    }
}

```



```java

public class DemoTest implements Runnable {

    private OrderIdGenerator orderIdGenerator = new OrderIdGenerator();

    //private Lock lock = new ZookeeperLock01();
    //private Lock lock = new ZookeeperLock02();
    private Lock lock = new RedisLock();

    @Override
    public void run() {
        try {
            lock.lock();
            String id = orderIdGenerator.get();
            System.out.println(Thread.currentThread().getName() + "\t:" + id);
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(new DemoTest()).start();
        }
    }
}

```



针对redis集群

RedLock方案

redission包实现