package code.queue.kafka;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
/**
 * 消费者 spring-kafka 2.0 + 依赖JDK8
 * @author 科帮网 By https://blog.52itstyle.com
 */
@Component
public class KafkaConsumer {

	@Autowired
	private IService service;
    /**
     * 监听seckill（channel）主题,有消息就读取
     * @param message
     */
    @KafkaListener(topics = {"seckill"})
    public void receiveMessage(String message){
        //收到通道的消息之后执行秒杀操作
        String[] array = message.split(";");
        // TODO 执行消费程序调用

    }
}