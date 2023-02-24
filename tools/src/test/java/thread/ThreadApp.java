package thread;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * TODO 有很多奇怪的问题，设置位置的变动结果会发生很大的改变
 * https://mp.weixin.qq.com/s?__biz=MzIzNzgyMjYxOQ==&mid=2247484380&idx=1&sn=ff2e2aaf9cfc63ae60cedd07dea26733&chksm=e8c3f428dfb47d3e5c9019d8048de1be45a1f0a40ccb5e4566e38f5988e7c6ba085a5885fcdd&mpshare=1&scene=24&srcid=0508dPfcozaNCAVZrjBceqli&sharer_sharetime=1589006234715&sharer_shareid=cded50ac01d784d35c8ca2a1912ee86e#rd
 * https://www.cnblogs.com/throwable/p/12817754.html
 */
@Slf4j
public class ThreadApp {

    static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 2, 100, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));

    public static void handler01(){
        // 1. 获取父线程
        String wrapper = "THREAD_LOCAL";
        TtlParameterWrapper.setCaller(wrapper, wrapper);
        log.info("父线程的值 ->{}",TtlParameterWrapper.getCaller(wrapper));
        CompletableFuture.runAsync(()->{
            // 2. 设置子线程的值，复用
            TtlParameterWrapper.setCaller(wrapper, wrapper);
            log.info("{}子线程的值 ->{}", wrapper, TtlParameterWrapper.getCaller(wrapper));
        });
    }

    public static void handler02(){
        // 1. 获取父线程
        String wrapper = "INHERITABLE_THREAD_LOCAL";
        TtlParameterWrapper.setCaller(wrapper, wrapper);
        log.info("父线程的值 ->{}",TtlParameterWrapper.getCaller(wrapper));
        CompletableFuture.runAsync(()->{
            log.info("{} 001 子线程的值 ->{}", wrapper, TtlParameterWrapper.getCaller(wrapper));
            CompletableFuture.runAsync(()->{
                log.info("{} 002 子线程的值 ->{}", wrapper, TtlParameterWrapper.getCaller(wrapper));
                TtlParameterWrapper.setCaller(wrapper, "更新值"+wrapper);
            });
        });

        ThreadUtil.sleep(500);
        CompletableFuture.runAsync(()->{
            log.info("{} 003 子线程的值 ->{}", wrapper, TtlParameterWrapper.getCaller(wrapper));
        });
    }

    public static void handler02_pool(){
        // 1. 获取父线程
        String wrapper = "INHERITABLE_THREAD_LOCAL";
        TtlParameterWrapper.setCaller(wrapper, wrapper);
        log.info("父线程的值 ->{}",TtlParameterWrapper.getCaller(wrapper));
        EXECUTOR.execute(()->{
            TtlParameterWrapper.setCaller(wrapper, "更新值"+wrapper);
            log.info("{} 001 子线程的值 ->{}", wrapper, TtlParameterWrapper.getCaller(wrapper));
            EXECUTOR.execute(()->{
                log.info("{} 002 子线程的值 ->{}", wrapper, TtlParameterWrapper.getCaller(wrapper));

                //TtlParameterWrapper.setCaller(wrapper, "更新值"+wrapper);
            });
        });
        ThreadUtil.sleep(500);
        // 首次变量传递成功是因为线程池中的所有子线程都是派生自main线程。此时线程处理main线程，已经有值，不在进行更新
        // TtlParameterWrapper.setCaller(wrapper, "更新值"+wrapper);
        EXECUTOR.execute(()->{
            log.info("{} 003 子线程的值 ->{}", wrapper, TtlParameterWrapper.getCaller(wrapper));
        });
    }

    public static void handler03(){
        // 1. 获取父线程
        String wrapper = "TRANSMITTABLE_THREAD_LOCAL";
        TtlParameterWrapper.setCaller(wrapper, wrapper);
        log.info("父线程的值 ->{}",TtlParameterWrapper.getCaller(wrapper));
        CompletableFuture.runAsync(()->{
            log.info("{} 001 子线程的值 ->{}", wrapper, TtlParameterWrapper.getCaller(wrapper));
            CompletableFuture.runAsync(()->{
                log.info("{} 002 子线程的值 ->{}", wrapper, TtlParameterWrapper.getCaller(wrapper));
                TtlParameterWrapper.setCaller(wrapper, "更新值"+wrapper);
            });
        });

        CompletableFuture.runAsync(()->{
            log.info("{} 003 子线程的值 ->{}", wrapper, TtlParameterWrapper.getCaller(wrapper));
        });
    }

    public static void handler03_pool(){
        // 1. 获取父线程
        String wrapper = "TRANSMITTABLE_THREAD_LOCAL";
        TtlParameterWrapper.setCaller(wrapper, wrapper);
        log.info("父线程的值 ->{}",TtlParameterWrapper.getCaller(wrapper));
        EXECUTOR.execute(()->{
            log.info("{} 001 子线程的值 ->{}", wrapper, TtlParameterWrapper.getCaller(wrapper));
            EXECUTOR.execute(()->{
                log.info("{} 002 子线程的值 ->{}", wrapper, TtlParameterWrapper.getCaller(wrapper));
                TtlParameterWrapper.setCaller(wrapper, "更新值"+wrapper);
            });
        });

        ThreadUtil.sleep(500);
        //  TtlParameterWrapper.setCaller(wrapper, "更新值"+wrapper);
        EXECUTOR.execute(()->{
            log.info("{} 003 子线程的值 ->{}", wrapper, TtlParameterWrapper.getCaller(wrapper));
        });
    }



    public static void main(String[] args) {
        //handler01();

        //handler02();
        handler02_pool();

        //handler03();
        // 必需要等待
        //handler03_pool();

        ThreadUtil.sleep(1000);
    }

}
