package utils.multi_thread.threadpool;

import java.util.concurrent.*;

/**
 * 我们知道线程池主要有四种拒绝策略，如下：
 *
 * AbortPolicy: 丢弃任务并抛出RejectedExecutionException异常。(默认拒绝策略)
 * DiscardPolicy：丢弃任务，但是不抛出异常。
 * DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务。
 * CallerRunsPolicy：由调用方线程处理该任务。
 * 如果线程池拒绝策略设置不合理，就容易有坑。我们把拒绝策略设置为DiscardPolicy或DiscardOldestPolicy并且在被拒绝的任务，
 * Future对象调用get()方法,那么调用线程会一直被阻塞。
 */
public class DiscardThreadPoolTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        // 一个核心线程，队列最大为1，最大线程数也是1.拒绝策略是DiscardPolicy
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(1), new ThreadPoolExecutor.DiscardPolicy());

        Future f1 = executorService.submit(() -> {
            System.out.println("提交任务1");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "完成任务1";
        });

        Future f2 = executorService.submit(() -> {
            System.out.println("提交任务2");
            return "完成任务2";
        });

        Future f3 = executorService.submit(() -> {
            System.out.println("提交任务3");
            return "完成任务3";
        });

        System.out.println("任务1完成 " + f1.get());// 等待任务1执行完毕
        System.out.println("任务2完成 " + f2.get());// 等待任务2执行完毕

        System.out.println("任务3完成 " + f3.get());// 等待任务3执行完毕
        /*try {
            System.out.println("任务3完成 " + f3.get(10, TimeUnit.SECONDS));// 等待任务3执行完毕
        }catch (Exception e){
            e.printStackTrace();
        }*/
        System.out.println("================");

        executorService.shutdown();// 关闭线程池，阻塞直到所有任务执行完毕

    }
}
