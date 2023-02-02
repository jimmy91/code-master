package code.queue.redis;

import org.springframework.stereotype.Service;
/**
 * 消费者
 * @author 科帮网 By https://blog.52itstyle.com
 */
@Service
public class RedisConsumer {

	
    public void receiveMessage(String message) {
		//收到通道的消息之后执行秒杀操作(超卖)
		String[] array = message.split(";");
		//  TODO 执行消费程序调用
	}
}