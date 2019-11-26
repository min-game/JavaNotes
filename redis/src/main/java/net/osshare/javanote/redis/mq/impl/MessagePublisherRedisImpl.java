package net.osshare.javanote.redis.mq.impl;

import net.osshare.javanote.redis.mq.MessagePublisher;
import net.osshare.javanote.redis.mq.RedisConnection;
import redis.clients.jedis.Jedis;

/**
 * 消息生产者服务 redis 实现类
 */
public class MessagePublisherRedisImpl implements MessagePublisher {
    private RedisConnection redisConnection;

    /**
     * 可以同时向多个频道发送数据
     */
    private String[] channels;

    public void setRedisConnection(RedisConnection redisConnection) {
        this.redisConnection = redisConnection;
    }

    public void setChannels(String[] channels) {
        this.channels = channels;
    }

    public boolean sendMessage(String message) {
        Jedis jedis = null;
        try {
            if (channels != null && channels.length > 0) {
                jedis = redisConnection.getJedis();
                for (String channel : channels) {
                    jedis.publish(channel, message);
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }
}