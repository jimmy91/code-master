package thread;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * https://blog.csdn.net/weixin_44742132/article/details/121368970
 *
 * 当corePoolSize<4 必定会导致无法正常执行完成
 * 当corePoolSize>=4 for多次执行的情况下，也会死锁，
 * 原因：主任务等子任务执行完成，但子任务在线程池在队列中无法获得运行线程机会。
 */
public class TestThreadPoolDeadlock {

    static ThreadPoolExecutor executor = new ThreadPoolExecutor(4,10,1L, TimeUnit.SECONDS,new ArrayBlockingQueue<>(100),new ThreadPoolExecutor.CallerRunsPolicy());


    public static void main(String[] args) {

        TestThreadPoolDeadlock deadlock = new TestThreadPoolDeadlock();
        deadlock.run();

    }

    public void run() {
        for (int i = 1; i < 10 ; i++){
            executor.execute( new Thread(new MainTask("任务" + i)));
        }

    }

    public class MainTask implements Runnable{

        private String name;

        MainTask(String name){
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println("主任务执行开始...." + name);
            ThreadUtil.sleep(1000);
            CountDownLatch latch = new CountDownLatch(2);
            executor.execute( new Thread(new SubTask(name+"子任务01", latch)));
            executor.execute( new Thread(new SubTask(name+"子任务02", latch)));
            try {
                latch.await();
                //latch.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("主任务执行结束...." + name);

        }
    }


    public class SubTask implements Runnable{

        private String name;

        CountDownLatch latch;

        SubTask(String name, CountDownLatch latch){
            this.name = name;
            this.latch = latch;
        }

        @Override
        public void run() {
            ThreadUtil.sleep(1000);
            System.out.println("子任务执行....." + name);
            latch.countDown();
        }
    }

}
