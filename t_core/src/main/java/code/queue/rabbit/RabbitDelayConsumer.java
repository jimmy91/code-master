package code.queue.rabbit;

import code.trace.TraceInterceptor;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import utils.generator.common.exception.ServiceException;


/**
 * @author: bright
 * @description:
 * @time: 2022-06-11 10:17
 */
@Component
@Slf4j
public class RabbitDelayConsumer {

    /**
     * 普通消费
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(queues = RabbitX_DelayedMessage.MQ_DELAY_QUEUE)
    @Transactional(rollbackFor = Exception.class)
    public void receiveDelayedQueue(Message message, Channel channel) throws Exception {
        try {
            // 链路追踪
            MDC.put(TraceInterceptor.TRACE_ID, message.getMessageProperties().getHeader(TraceInterceptor.TRACE_ID).toString());
            String userId = message.getMessageProperties().getHeader("user-id").toString();
            log.info("rabbit 延时(x-delayed)消费获取：userId={} {}", userId, new String(message.getBody(), "utf-8"));
            //手动确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            if (e instanceof ServiceException) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
            //丢到死信队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            e.printStackTrace();
        }
    }


    /**
     * 死信消费
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(queues = RabbitTTLMessage.MQ_REPEAT_QUEUE_NAME)
    @Transactional(rollbackFor = Exception.class)
    public void receiveDelayedDeadLetterQueue(Message message, Channel channel) throws Exception {
        try {
            // 链路追踪
            MDC.put(TraceInterceptor.TRACE_ID, message.getMessageProperties().getHeader(TraceInterceptor.TRACE_ID).toString());
            String userId = message.getMessageProperties().getHeader("user-id").toString();
            log.info("rabbit 延时(TTL)消费获取：userId={} {}", userId, new String(message.getBody(), "utf-8"));
            //手动确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            if (e instanceof ServiceException) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
            //丢到死信队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            e.printStackTrace();
        }
    }

}
