package code.queue.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
/**
 * 消费者
 * @author 科帮网 By https://blog.52itstyle.com
 */
@Service
@Slf4j
public class RedisConsumer {

    public void receiveMessage(String message) {
		// 收到通道的消息
		//  TODO 执行消费程序调用
		log.info("接收到消息 message={}", message);
	}
}