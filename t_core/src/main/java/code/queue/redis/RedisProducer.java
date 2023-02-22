package code.queue.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
 
/**
 * 生产者
 * @author Jimmy
 */
@Service
public class RedisProducer {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 向通道发送消息的方法
     */
    public void sendChannelMess(String topic, String message) {
        stringRedisTemplate.convertAndSend(RedisSubListenerConfig.TOPIC, message);
    }
}
