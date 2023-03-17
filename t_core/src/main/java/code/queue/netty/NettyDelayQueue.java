package code.queue.netty;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 时间轮
 * 当添加一个定时、延时任务A，假如会延迟25秒后才会执行，可时间轮一圈round 的长度才24秒，那么此时会根据时间轮长度和刻度得到一个圈数 round和对应的指针位置 index，
 * 也是就任务A会绕一圈指向0格子上，此时时间轮会记录该任务的round和 index信息。当round=0，index=0 ，指针指向0格子 任务A并不会执行，因为 round=0不满足要求
 * https://www.cnblogs.com/chengxy-nds/p/12844942.html
 * @author Jimmy
 */
public class NettyDelayQueue {

    public static void main(String[] args) {

        /**
         * ThreadFactory ：表示用于生成工作线程，一般采用线程池；
         * tickDuration和unit：每格的时间间隔，默认100ms；
         * ticksPerWheel：一圈下来有几格，默认512，而如果传入数值的不是2的N次方，则会调整为大于等于该参数的一个2的N次方数值，有利于优化hash值的计算。
         */
        final Timer timer = new HashedWheelTimer(Executors.defaultThreadFactory(), 5, TimeUnit.SECONDS, 2);

        //定时任务
        TimerTask task1 = new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                System.out.println("order1  5s 后执行 ");
                timer.newTimeout(this, 5, TimeUnit.SECONDS);//结束时候再次注册
            }
        };
        timer.newTimeout(task1, 5, TimeUnit.SECONDS);
        TimerTask task2 = new TimerTask() {
            public void run(Timeout timeout) throws Exception {
                System.out.println("order2  10s 后执行");
                timer.newTimeout(this, 10, TimeUnit.SECONDS);//结束时候再注册
            }
        };

        timer.newTimeout(task2, 10, TimeUnit.SECONDS);

        //延迟任务
        timer.newTimeout(new TimerTask() {
            public void run(Timeout timeout) throws Exception {
                System.out.println("order3  15s 后执行一次");
            }
        }, 15, TimeUnit.SECONDS);

    }
}
