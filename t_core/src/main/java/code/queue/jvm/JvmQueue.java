package code.queue.jvm;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * jvm队列(固定长度为100)
 *
 * @author Jimmy
 */
public class JvmQueue<T> {

    /**
     * 队列大小
     */
    static final int QUEUE_MAX_SIZE = 100;

    BlockingQueue<T> blockingQueue = new LinkedBlockingQueue<T>(QUEUE_MAX_SIZE);

    /**
     * 秒杀单例队列
     */
    public static JvmQueue getSeckillQueue() {
        return SingletonHolder.queue;
    }

    /**
     * 私有的默认构造子，保证外界无法直接实例化
     */
    private JvmQueue() {
    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
     */
    private static class SingletonHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static JvmQueue queue = new JvmQueue();
    }

    /**
     * 生产入队
     * @param kill
     * <p>
     * add(e) 队列未满时，返回true；队列满则抛出IllegalStateException(“Queue full”)异常——AbstractQueue
     * put(e) 队列未满时，直接插入没有返回值；队列满时会阻塞等待，一直等到队列未满时再插入。
     * offer(e) 队列未满时，返回true；队列满时返回false。非阻塞立即返回。
     * offer(e, time, unit) 设定等待的时间，如果在指定时间内还不能往队列中插入数据则返回false，插入成功返回true。
     */
    public Boolean produce(T kill) throws InterruptedException {
        return blockingQueue.offer(kill);
    }

    /**
     * 消费出队
     *
     * @return
     * @throws InterruptedException
     */
    public T consume() throws InterruptedException {
        return blockingQueue.take();
    }

    /**
     * 获取队列大小
     *
     * @return
     */
    public int size() {
        return blockingQueue.size();
    }
}
