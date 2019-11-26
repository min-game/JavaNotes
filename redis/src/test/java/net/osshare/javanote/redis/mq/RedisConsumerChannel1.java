package net.osshare.javanote.redis.mq;

import net.osshare.javanote.redis.mq.impl.MessageConsumerRedisImpl;
import org.junit.Test;

/**
 * 消息消费服务测试类
 */
public class RedisConsumerChannel1 {

    @Test
    public void consumerTest() {
        RedisConnection redisConnection = RedisConnectionUtil.create();
        new MessageConsumerRedisImpl(redisConnection, new String[]{"channel1"}) {
            public void handleMessage(String message) {
                System.out.println(message);
            }
        };
    }

}