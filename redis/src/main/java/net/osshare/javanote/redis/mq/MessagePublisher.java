package net.osshare.javanote.redis.mq;

/**
 * 消息生产者服务接口
 */
public interface MessagePublisher {
    boolean sendMessage(String message);
}