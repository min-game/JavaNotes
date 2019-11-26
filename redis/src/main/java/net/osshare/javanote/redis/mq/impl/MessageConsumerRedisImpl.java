package net.osshare.javanote.redis.mq.impl;

import net.osshare.javanote.redis.mq.MessageConsumer;
import net.osshare.javanote.redis.mq.RedisConnection;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * 消息消费者服务 redis 实现类
 */
public abstract class MessageConsumerRedisImpl implements MessageConsumer {

    /**
     *
     * @param redisConnection redis 连接类
     * @param channels 订阅的频道列表
     */
    public MessageConsumerRedisImpl(RedisConnection redisConnection, String[] channels) {
        Jedis jedis = null;
        try {
            if (channels != null && channels.length > 0) {
                jedis = redisConnection.getJedis();
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        System.out.println("receive " + message + " from " + channel);
                        handleMessage(message);
                    }
                }, channels);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}