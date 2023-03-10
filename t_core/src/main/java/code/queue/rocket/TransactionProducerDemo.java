package code.queue.rocket;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * RocketMQ的事务消息机制可以帮助我们处理分布式系统中的消息一致性问题，避免出现因为网络延迟、节点故障等原因导致的数据不一致问题。
 * RocketMQ的事务消息可以保证在消息发送方和消息接收方之间实现分布式事务的一致性。其基本原理如下：
 *
 * 事务消息的发送方先向消息服务端发送半消息。半消息包含了事务消息的业务逻辑和相应的属性，但是在此时并不会将消息真正发送给消息接收方。
 * 发送方会在本地事务执行成功后，向消息服务端发送确认消息。
 * 如果发送方在一定时间内没有发送确认消息，消息服务端将会回滚该半消息。
 * 当消息服务端收到确认消息时，会将半消息标记为“可投递”状态，并开始将消息发送给消息接收方。
 * 如果消息接收方成功处理该事务消息，则将其标记为“已消费”状态。
 * 如果消息接收方未能成功处理该事务消息，则将其标记为“未消费”状态。
 * 如果消息接收方在一定时间内未能成功处理该事务消息，则消息服务端会将该消息回滚，并将其标记为“可投递”状态，等待下一次重新发送。
 * 通过上述机制，RocketMQ的事务消息实现了消息发送方和消息接收方之间的事务一致性。同时，RocketMQ还提供了消息回查机制，用于在消息处理失败时进行补偿处理，从而提高了消息的可靠性和可用性。
 * @author Jimmy
 * https://blog.csdn.net/Weixiaohuai/article/details/123733518
 * 值得注意的是，rocketmq并不会无休止的的信息事务状态回查，默认回查15次，如果15次回查还是无法得知事务状态，rocketmq默认回滚该消息。
 */
public class TransactionProducerDemo {

    public static void producer() throws MQClientException {

        TransactionMQProducer producer = new TransactionMQProducer("producer_group_name");
        producer.setNamesrvAddr("localhost:9876");

        // 创建事务监听器
        TransactionListener transactionListener = new TransactionListenerImpl();

        // 设置事务监听器
        producer.setTransactionListener(transactionListener);

        // 启动生产者
        producer.start();

        try {
            // 创建一个消息实例，包括 topic、tag 和 消息体
            Message msg = new Message("TopicTest", "TagA", "Hello RocketMQ".getBytes());

            // 发送事务消息
            TransactionSendResult sendResult = producer.sendMessageInTransaction(msg, new LocalTransactionExecuter() {
                // 事务消息不支持延时消息和批量消息；
                @Override
                public LocalTransactionState executeLocalTransactionBranch(Message message, Object o) {
                    // 执行本地事务
                    // 如果本地事务执行成功，返回 COMMIT_MESSAGE
                    // 如果本地事务执行失败，返回 ROLLBACK_MESSAGE
                    // 如果无法确定本地事务执行状态，返回 UNKNOW
                    return LocalTransactionState.UNKNOW;
                }
            }, null);

            // 输出发送结果
            System.out.printf("%s%n", sendResult);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭生产者
            producer.shutdown();
        }
    }

    static class TransactionListenerImpl implements TransactionListener {

        @Override
        public LocalTransactionState executeLocalTransaction(Message message, Object o) {
            // 执行本地事务
            // 如果本地事务执行成功，返回 COMMIT_MESSAGE
            // 如果本地事务执行失败，返回 ROLLBACK_MESSAGE
            // 如果无法确定本地事务执行状态，返回 UNKNOW
            return LocalTransactionState.UNKNOW;
        }

        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
            // 检查本地事务状态
            // 如果本地事务已经执行成功，返回 COMMIT_MESSAGE
            // 如果本地事务已经执行失败，返回 ROLLBACK_MESSAGE
            // 如果本地事务状态未知，返回 UNKNOW
            return LocalTransactionState.COMMIT_MESSAGE;
        }
    }


    public static void consumer() throws MQClientException {
        // 创建一个PushConsumer，指定消费组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("TransactionConsumerGroup");

        // 设置NameServer的地址
        consumer.setNamesrvAddr("localhost:9876");

        // 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
        // 如果不是第一次启动，那么按照上次消费的位置继续消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        // 订阅一个Topic，指定Tag为"TransactionMessageTag"
        consumer.subscribe("TransactionMessageTopic", "TransactionMessageTag");

        // 注册回调实现类来处理从broker拉取回来的消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            // 监听类实现MessageListenerConcurrently接口即可，重写consumeMessage方法接收数据
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgList, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (MessageExt messageExt : msgList){
                    try {
                        String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
                        System.out.println("消费者接收到消息: " + messageExt.toString() + "---消息内容为：" + body);
                        // 标记该消息已经被成功消费
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }catch (Exception e){
                        // 处理消息异常，需要回滚事务
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        // 启动Consumer实例，并指定MessageModel为集群消费模式
        consumer.start();
    }
}
