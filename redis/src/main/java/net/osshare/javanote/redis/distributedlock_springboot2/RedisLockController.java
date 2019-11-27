package net.osshare.javanote.redis.distributedlock_springboot2;

import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

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