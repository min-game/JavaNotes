package net.osshare.javanote.redis.mq;

import net.osshare.javanote.redis.mq.impl.MessagePublisherRedisImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 消息发送服务测试类
 */
public class RedisPublisherTest {
    private MessagePublisherRedisImpl messagePublisherRedis;

    @Before
    public void before() {
        RedisConnection redisConnection = RedisConnectionUtil.create();
        messagePublisherRedis = new MessagePublisherRedisImpl();
        messagePublisherRedis.setRedisConnection(redisConnection);
        messagePublisherRedis.setChannels(new String[]{"channel1", "channel2"});
    }

    @Test
    public void publisherTest() {
        for (int i = 0; i < 3; i++) {
            Assert.assertTrue(messagePublisherRedis.sendMessage("message" + i));
        }
    }
}