## SpringBoot使用RedisLockRegistry实现Redis分布式锁

``` text
RedisLockRegistry是spring-integration-redis中提供redis分布式锁实现类。
主要是通过redis锁＋本地锁双重锁的方式实现的一个比较好的锁。
```

- 加入Spring Integration依赖
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-integration</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.integration</groupId>
  <artifactId>spring-integration-redis</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

- 初始化 <font color="#ff502c">RedisLockRegistry</font>

``` java
@Configuration
public class RedisLockConfiguration {
  @Bean
  public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
    return new RedisLockRegistry(redisConnectionFactory, "registryKey"，5000L);
  }
}
```
``` text
注：RedisLockRegistry参数 
    registryKey:注册key(redis key的前缀，redis存放的key格式是 registryKey:lockKey)
    timeout:redis key的超时时间(默认6秒)
``` 

- 使用
``` java
@RestController
public class RedisLockController {
    @Resource
    private RedisLockRegistry redisLockRegistry;

    @GetMapping("/lock")
    public String lock(@RequestParam("lockKey") String lockKey) {
        Lock lock = redisLockRegistry.obtain(lockKey);
        String result = "";
        try {
            //获取锁 先获取本地锁ReentrantLock 如果本地锁存在则tryLock最多尝试3秒后返回结果 如果本地锁不存在则获取Redis锁 
            boolean getLock = lock.tryLock(3, TimeUnit.SECONDS);
            if(getLock) {
                //TODO 具体业务
            }
        } catch (Exception e) {
            result= "获取锁失败";
        } finally {
            //释放锁
            try {
                lock.unlock();
            }catch (Exception unex){
            }
        }
        return result;
    }
}
```