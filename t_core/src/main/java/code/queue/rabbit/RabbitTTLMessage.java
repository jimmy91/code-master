package code.queue.rabbit;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟队列实现原理：
 *      RabbitMQ的高级特性TTL ( Time-To-Live 存活时间 ) + 死信队列实现
 *      RabbitMQ可以对消息设置过期时间，也可以对整个队列（Queue）设置过期时间。
 * https://blog.csdn.net/weixin_42950079/article/details/125722595
 * https://blog.csdn.net/zitian246/article/details/121969181
 * @author Jimmy
 * 消息发送到交换机normal_exchange，然后路由到队列normal_queue上 因为队列normal_queue没有消费者，消息过期后成为死信消息
 * 死信消息携带设置的x-dead-letter-routing-key=dlx.test进入到死信交换机dlx_exechage dlx_exechage与dlx_queue绑定的routing key为"dlx.*"，
 * 死信消息的路由键dlx.test符合该规则被路由到dlx.queue上面。然后我们给死信队列添加消费者
 */
@Configuration
public class RabbitTTLMessage {

    private static final String MQ_DELAY_KEY = "app.mq.delay.message.ttl.";
    /**
     * 交换机, 是用来接收已过期的队列信息并进行重新分配队列进行消费
     */
    public static final String MQ_EXCHANGE = MQ_DELAY_KEY + "exchange";
    /**
     * 进行延时消费队列, 重新分配的队列名
     */
    public static final String MQ_REPEAT_QUEUE_NAME = MQ_DELAY_KEY + "repeat_queue_name";
    /**
     * 进行延时消费路由
     */
    public static final String MQ_DELAY_QUEUE = MQ_DELAY_KEY + "delay_queue";


    /**
     * 交换机用于重新分配队列
     */
    @Bean
    DirectExchange exchange() {
        return new DirectExchange(MQ_EXCHANGE);
    }

    /**
     *  用于延时消费的队列
     */
    @Bean
    public Queue repeatTradeQueue() {
        Queue queue = new Queue(MQ_REPEAT_QUEUE_NAME,true,false,false);
        return queue;
    }

    /**
     * 将repeatTradeQueue队列与exchange交换机绑定，并指定对应的routing key
     * @return
     */
    @Bean
    public Binding  repeatTradeBinding() {
        return BindingBuilder.bind(repeatTradeQueue()).to(exchange()).with(MQ_REPEAT_QUEUE_NAME);
    }

    /**
     * 配置死信队列
     */
    @Bean
    public Queue deadLetterQueue() {
        Map<String,Object> args = new HashMap<>(16);
        //队列的消息 TTL 存活时间 (单位ms)，如果发送消息时也设置了过期时间，取最小值
        args.put("x-message-ttl", 20 * 1000);
        //x-dead-letter-exchange是指过期消息重新转发到指定交换机
        args.put("x-dead-letter-exchange", MQ_EXCHANGE);
        //该交换机上绑定的routing-key，将通过配置的routing-key分配对应的队列，也就是前面配置的repeatTradeQueue。，如果不设置，则默认使用原队列的routing key
        args.put("x-dead-letter-routing-key", MQ_REPEAT_QUEUE_NAME);
        return new Queue(MQ_DELAY_QUEUE, true, false, false, args);
    }


}
