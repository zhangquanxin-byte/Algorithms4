# 基于redis的抢红包

把原始的红包称为大红包，拆分后的红包称为小红包。

## 小红包预先生成

```
生成算法http://blog.csdn.net/hengyunabc/article/details/19177877
```

## 两个redis队列

一个是未消费红包队列，另一个是已消费红包队列。

开始时，把未抢的小红包全放到未消费红包队列里。json字符串，如{id:'789', money:'300'}

## 用一个 set 来过滤已抢到红包的用户

已经抢到红包的用户加入set, 存每个userId

## 抢红包

先判断用户是否抢过红包，如果没有，

从未消费红包队列中取出一个小红包(rpop)

再push到另一个已消费队列中(加入userId) , json字符串，如{id:'789', money:'300', userId:'admin'}

最后把用户ID放入去重的set中

## 原子执行

采用lua脚本方式

# 代码

## demo .lua

```lua
-- KEYS[1] 未抢红包列表list
-- KEYS[2] 已抢红包列表list
-- KEYS[3] 抢到的人set
-- KEYS[4] userId
--
-- set中存在userId, 已经抢到了
-- function (KEYS[1] KEYS[2] KEYS[1] KEYS[1]){ sismeneber userset admin
if redis.call('sismember', KEYS[3], KEYS[4]) ~= 0 then
    return nil
else
    -- 从未抢红包列表 pop 出一个红包
    local bao = redis.call('rpop', KEYS[1]);
    if bao then
        -- lua-cjson 是一个简单小巧的开源动态库
        -- Redis服务端,cjson开源库以及Lua环境,都已编译进去
        local s = cjson.decode(bao);
        -- add userId
        s['userId'] = KEYS[4];
        local re = cjson.encode(s);
        -- 加入已抢红包列表list
        redis.call('lpush', KEYS[2], re);
        -- 加入set
        redis.call('sadd', KEYS[3], KEYS[4]);
        return re;
    end
end
return nil
--)
```

## pom

```xml
	<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.60</version>
        </dependency>

    </dependencies>
```

## application

```properties
spring.redis.host=192.168.5.55
spring.redis.port=6379
```

## main

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext run = SpringApplication.run(Main.class);
        HongBaoService bean = run.getBean(HongBaoService.class);
        bean.test();
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        return new StringRedisTemplate(lettuceConnectionFactory);
    }

    //redis使用脚本的配置
    @Bean
    public DefaultRedisScript<String> defaultRedisScript() {
        DefaultRedisScript<String> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setResultType(String.class);
        defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("demo.lua")));
        System.out.println("defaultRedisScript = " + defaultRedisScript.getScriptAsString());
        return defaultRedisScript;
    }
}
```

## service

```java
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Service
public class HongBaoService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DefaultRedisScript defaultRedisScript;

    private HongBaoService() {
        System.out.println("==== HongBaoService");
    }

    static int hongBaoCount = 1000;

    static int threadCount = 20;

    static byte[] scriptSha;

    // 红包池
    static String poolList = "pool:list";
    // 已经抢到红包的人
    static String userSet = "user:set";
    // 已经抢到红包的明细
    static String userList = "user:list";

    public void test() throws Exception {
        create();
        load();
        get();
    }

    public void create() throws Exception {
        final CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; ++i) {
            final int temp = i;
            Thread thread = new Thread(() -> {
                int part = hongBaoCount / threadCount;
                JSONObject object = new JSONObject();
                for (int id = temp * part; id < (temp + 1) * part; id++) {
                    object.put("id", id);
                    object.put("money", id);
                    stringRedisTemplate.opsForList().leftPush(poolList, object.toJSONString());
                    //System.out.println("push:" + object.toJSONString());
                }
                latch.countDown();
            });
            thread.start();
        }
        latch.await();
    }

    // RedisTemplate预加载lua到redis
    public void load() {
        stringRedisTemplate.execute((RedisConnection connection) -> {
            String s = connection.scriptLoad(defaultRedisScript.getScriptAsString().getBytes());
            scriptSha = s.getBytes();
            System.out.println("scriptSha = " + s);
            return scriptSha;
        });
    }

    public void get() throws Exception {
        StopWatch watch = new StopWatch();
        final CountDownLatch latch = new CountDownLatch(threadCount);
        watch.start();
        for (int i = 0; i < threadCount; ++i) {
            Thread thread = new Thread(() -> {
                while (true) {
                    Object result = stringRedisTemplate.execute((RedisConnection connection) -> {
                        String userId = UUID.randomUUID().toString() + "-" + Thread.currentThread().getId();
                        //return connection.eval(defaultRedisScript.getScriptAsString().getBytes(), ReturnType.VALUE, 4,
                        //        poolList.getBytes(), userList.getBytes(), userSet.getBytes(), userId.getBytes());
                        return connection.evalSha(scriptSha, ReturnType.VALUE, 4,
                                poolList.getBytes(), userList.getBytes(), userSet.getBytes(), userId.getBytes());
                    });
//                    if (result != null) {
//                        System.out.println("get :" + new String((byte[]) result));
//                    } else {
//                        //已经取完了
//                        if (stringRedisTemplate.opsForList().size(poolList) == 0) {
//                            break;
//                        }
//                    }
                    //已经取完了
                    if (stringRedisTemplate.opsForList().size(poolList) == 0) {
                        break;
                    }
                }
                latch.countDown();
            });
            thread.start();
        }

        latch.await();
        watch.stop();

        System.out.println("time:" + watch.getTotalTimeSeconds());
        System.out.println("speed:" + hongBaoCount / watch.getTotalTimeSeconds());
    }
}
```

