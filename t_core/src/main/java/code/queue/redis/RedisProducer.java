package code.queue.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
 
/**
 * 生产者
 * @author Jimmy
 * https://blog.csdn.net/vipshop_fin_dev/article/details/120295037
 */
@Service
@Slf4j
public class RedisProducer {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 向通道发送消息的方法
     */
    public void sendChannelMess(String topic, String message) {
        log.info("redis 发送Topic消息");
        stringRedisTemplate.convertAndSend(RedisSubListenerConfig.TOPIC, message);
    }
}
