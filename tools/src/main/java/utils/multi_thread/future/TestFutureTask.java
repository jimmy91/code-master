package utils.multi_thread.future;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author PLXQ
 */
public class TestFutureTask {

    public static void main(String[] args) throws Exception {
        FutureTask<String> ft1 = new FutureTask<>(new Task1());
        FutureTask<String> ft2 = new FutureTask<>(new Task2(ft1));
        Thread t1 = new Thread(ft1);
        t1.start();
        Thread t2 = new Thread(ft2);
        t2.start();
        System.out.println(ft2.get());
    }

    static class Task2 implements Callable<String> {
        private FutureTask<String> futureTask;

        public Task2(FutureTask futureTask) {
            this.futureTask = futureTask;
        }

        @Override
        public String call() throws Exception {
            System.out.println("线程2：吸水壶");
            Thread.sleep(1000);
            System.out.println("线程2：烧开水");
            Thread.sleep(1000);
            // 等待task1完成
            String result = futureTask.get();
            System.out.println("线程2：拿到茶叶：" + result);
            System.out.println("线程2：泡茶");
            Thread.sleep(1000);
            return "上茶喽...";
        }
    }

    static class Task1 implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("线程1：洗茶壶");
            Thread.sleep(1000);
            System.out.println("线程1：洗茶杯");
            Thread.sleep(1000);
            System.out.println("线程1：拿茶叶");
            Thread.sleep(1000);
            return "龙井";
        }
    }
}


