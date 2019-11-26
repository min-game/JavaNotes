package net.osshare.javanote.redis.mq;

/**
 * 消息消费者服务接口
 */
public interface MessageConsumer {
    void handleMessage(String message);
}