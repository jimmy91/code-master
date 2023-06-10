package code.queue.rabbit;

import code.trace.TraceInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Jimmy
 * DLX + TTL 和 Delayed Message 插件这两种 RabbitMQ 延迟消息解决方案都有一定的局限性。
 * 如果你的消息 TTL 是相同的，使用 DLX + TTL 的这种方式是没问题的，对于我来说目前还是优选。
 * 如果你的消息 TTL 过期值是可变的，可以尝试下使用 Delayed Message 插件，对于某些应用而言它可能很好用，对于那些可能会达到高容量延迟消息应用而言，则不是很好。
 */
@Service
@Slf4j
public class RabbitProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;


    /**
     * 发送延时消息
     * 策略：消息过期->进行死信队列->消费死信队列
     * @param message
     * @return
     */
    public boolean sendTTLMess(Object message){
        log.info("rabbit 发送TTL消息");
        rabbitTemplate.convertAndSend(RabbitTTLMessage.MQ_DELAY_QUEUE, message, correlationData -> {
            // 设置过期时间8秒
            // TODO 自定义过期时间，可能会出现时序问题。同一队列中，先到的数据必须先到达过期时间，不然会影响队列数据过期失效删除。后到的消息如果小于前一个时间，会在前一个时间失效后同时失效
            correlationData.getMessageProperties().setExpiration("8000");
            // 消息持久化
            // correlationData.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            correlationData.getMessageProperties().setHeader("user-id", "loginUserId");
            // 链路追踪
            correlationData.getMessageProperties().setHeader(TraceInterceptor.TRACE_ID, MDC.get(TraceInterceptor.TRACE_ID));
            return correlationData;
        });
        return true;
    }

    /**
     * 我们可以声明 x-delayed-message 类型的 Exchange，消息发送时指定消息头 x-delay 以毫秒为单位将消息进行延迟投递。
     * @param message
     * @return
     */
    public boolean sendX_DelayMess(Object message){
        log.info("rabbit 发送x-delayed消息");
        rabbitTemplate.convertAndSend(RabbitX_DelayedMessage.MQ_EXCHANGE, RabbitX_DelayedMessage.MQ_DELAY_QUEUE, message, correlationData -> {
            //设置延迟时间
            correlationData.getMessageProperties().setDelay(5 * 1000);
            correlationData.getMessageProperties().setHeader("user-id", "loginUserId");
            // 链路追踪
            correlationData.getMessageProperties().setHeader(TraceInterceptor.TRACE_ID, MDC.get(TraceInterceptor.TRACE_ID));
            return correlationData;
        });

        return true;
    }

}
