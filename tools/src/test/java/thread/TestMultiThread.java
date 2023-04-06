package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestMultiThread {

    /**
     * 创建多线程的回调函数时，传入的参数会被当做一个引用保存起来，即使这个参数没有明显的对应到一个变量上。
     * 即使后来传入的参数指向了其他对象，但是多线程保存的引用是不会变的。
     *
     * 如果将参数从StringBuilder改为String，结果将发生变化，因为String是不能修改的，执行附加的时候实际上是生成了一个新的对象。
     * 但是对于已经创建好的多线程任务来说，他们保存的依然是以前的引用。
     * @throws InterruptedException
     */
    public static void testMultiThread() throws InterruptedException {
        final List<StringBuilder> strs = new ArrayList<StringBuilder>();
        strs.add(new StringBuilder("1"));
        strs.add(new StringBuilder("2"));
        strs.add(new StringBuilder("3"));
        strs.add(new StringBuilder("4"));

        final List<Callable<String>> tasks = new ArrayList<Callable<String>>();
        for (final StringBuilder str : strs) {
            tasks.add(new Callable<String>() {
                public String call() throws Exception {
                    System.out.println(str.append("Hello").append("@").append(str.hashCode()));
                    return str + "Hello";
                }
            });
        }

        // ！！！ 在外对参数进行了修改
        for (int i = 0; i < strs.size(); i++) {
            strs.get(i).append("new");
        }

        //添加插入条件

        for (StringBuilder str : strs) {
            System.out.println(str.hashCode());
        }

        System.out.println("==============");

        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.invokeAll(tasks);
    }


    public static void main(String[] args) throws InterruptedException {
        testMultiThread();
    }
}
