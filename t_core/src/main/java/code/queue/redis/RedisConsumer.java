package code.queue.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
/**
 * 消费者
 * @author Jimmy
 */
@Service
@Slf4j
public class RedisConsumer {

    public void receiveMessage(String message) {
		// 收到通道的消息
		//  TODO 执行消费程序调用,需要做重复消费处理
		log.info("redis【发布订阅模式（广播模式）】接收到消息 message={}", message);
	}
}