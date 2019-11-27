package net.osshare.javanote.redis.distributedlock_springboot2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

@Configuration
public class RedisLockConfiguration {
  @Bean
  public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
    //registryKey -> redis key的前缀，redis存放的key格式是 registryKey:lockKey
    return new RedisLockRegistry(redisConnectionFactory, "registryKey");
  }
}