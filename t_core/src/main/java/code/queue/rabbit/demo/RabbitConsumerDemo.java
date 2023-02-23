package code.queue.rabbit.demo;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 入门程序消费者
 *
 * @author Jimmy
 * @version 1.0
 **/
public class RabbitConsumerDemo {

    /**
     *    队列
     */
    private static final String QUEUE = "hello-world";

    public static void main(String[] args) throws Exception {

        //建立新连接
        Connection connection = ConnectionUtil.getConnection();
        //创建会话通道,生产者和mq服务所有通信都在channel通道中完成
        Channel channel = connection.createChannel();

        //监听队列
        //声明队列，如果队列在mq 中没有则要创建
        //参数：String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        /**
         * 参数明细
         * 1、queue 队列名称
         * 2、durable 是否持久化，如果持久化，mq重启后队列还在
         * 3、exclusive 是否独占连接，队列只允许在该连接中访问，如果connection连接关闭队列则自动删除,如果将此参数设置true可用于临时队列的创建
         * 4、autoDelete 自动删除，队列不再使用时是否自动删除此队列，如果将此参数和exclusive参数设置为true就可以实现临时队列（队列不用了就自动删除）
         * 5、arguments 参数，可以设置一个队列的扩展参数，比如：可设置存活时间
         */
        channel.queueDeclare(QUEUE, true, false, false, null);
        // 绑定队列到交换机，理解为：队列对来自此交换机的消息感兴趣
        // channel.queueBind(QUEUE, EXCHANGE_NAME, "");

        //指该消费者在接收到队列里的消息但没有返回确认结果之前,它不会将新的消息分发给它。
        //channel.basicQos(1);
        //实现消费方法
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {

            /**
             * 当接收到消息后此方法将被调用
             * @param consumerTag  消费者标签，用来标识消费者的，在监听队列时设置channel.basicConsume
             * @param envelope 信封，通过envelope
             * @param properties 消息属性
             * @param body 消息内容
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //交换机
                String exchange = envelope.getExchange();
                //消息id，mq在channel中用来标识消息的id，可用于确认消息已接收
                long deliveryTag = envelope.getDeliveryTag();
                //消息内容
                String message = new String(body, "utf-8");
                System.out.println("【1】receive message:" + message);
                // 消费者手动发送ack应答,需要将监听autoAck设计为false
                // channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        //监听队列
        //参数：String queue, boolean autoAck, Consumer callback
        /**
         * 参数明细：
         * 1、queue 队列名称
         * 2、autoAck 自动回复，当消费者接收到消息后要告诉mq消息已接收，如果将此参数设置为true表示会自动回复mq，如果设置为false要通过编程实现回复
         * 3、callback，消费方法，当消费者接收到消息要执行的方法
         */
        channel.basicConsume(QUEUE, true, defaultConsumer);

    }
}