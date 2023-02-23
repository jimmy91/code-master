package code.queue.rabbit;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟队列实现原理：
 *      RabbitMQ的高级特性TTL ( Time-To-Live 存活时间 ) + （DLX）死信队列实现
 *      RabbitMQ可以对消息设置过期时间，也可以对整个队列（Queue）设置过期时间。
 * https://blog.csdn.net/weixin_42950079/article/details/125722595
 * https://blog.csdn.net/zitian246/article/details/121969181
 * @author Jimmy
 */
@Configuration
public class RabbitX_DelayedMessage {

    private static final String MQ_DELAY_KEY = "app.mq.delay.message.x-delayed.";
    /**
     * 交换机, 是用来接收已过期的队列信息并进行重新分配队列进行消费
     */
    public static final String MQ_EXCHANGE = MQ_DELAY_KEY + "exchange";
    /**
     * 进行延时消费路由
     */
    public static final String MQ_DELAY_QUEUE = MQ_DELAY_KEY + "delay_queue";


    /**
     * 自定义的交换机类型
     * @return
     */
    @Bean
    CustomExchange delayedExchange() {
        Map<String,Object> args = new HashMap<>(16);
        args.put("x-delayed-type", "direct");
        return new CustomExchange(MQ_EXCHANGE,"x-delayed-message",true,false,args);
    }

    /**
     * 创建一个队列
     * @return
     */
    @Bean
    public Queue delayedQueue() {
        return new Queue(MQ_DELAY_QUEUE,true);
    }

    /**
     * 绑定队列到自定义交换机
     * @return
     */
    @Bean
    public Binding bindingNotify() {
        return BindingBuilder.bind(delayedQueue()).to(delayedExchange()).with(MQ_DELAY_QUEUE).noargs();
    }


}
