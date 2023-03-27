package app.config;

import code.trace.MdcTaskDecorator;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;


/**
 * @ClassName: MdcThreadPoolConfiguration
 * @Author Jimmy
 * @Description: 用于链路追踪MDC的线程池\在多线程情况下会将主线程的上下文传递给子线程
 *
 * 参考文档：https://www.cnblogs.com/dw3306/p/12551327.html
 *
 * 线程池参数的合理设置
 * 为了说明合理设置的条件，我们首先确定有以下几个相关参数：
 * 1.tasks，程序每秒需要处理的最大任务数量（假设系统每秒任务数为100~1000）
 * 2.tasktime，单线程处理一个任务所需要的时间（每个任务耗时0.1秒）
 * 3.responsetime，系统允许任务最大的响应时间（每个任务的响应时间不得超过2秒）
 *
 * corePoolSize
 * 每个任务需要tasktime秒处理，则每个线程每秒可处理1/tasktime个任务。系统每秒有tasks个任务需要处理，则需要的线程数为：tasks/(1/tasktime)。
 * 即tasks*tasktime个线程数。假设系统每秒任务数为100到1000之间，每个任务耗时0.1秒，则需要100x0.1至1000x0.1，即10到100个线程。那么corePoolSize应该设置为大于10。
 * 具体数字最好根据80、20原则，即80%情况下系统每秒任务数，若系统80%的情况下任务数小于200，最多时为1000，则corePoolSize可设置为20。
 *
 * queueCapacity：任务队列的长度
 * 任务队列的长度要根据核心线程数，以及系统对任务响应时间的要求有关。队列长度可以设置为(corePoolSize/tasktime)responsetime： (20/0.1)2=400，即队列长度可设置为400。
 * 如果队列长度设置过大，会导致任务响应时间过长，如以下写法：
 * LinkedBlockingQueue queue = new LinkedBlockingQueue();
 * 这实际上是将队列长度设置为Integer.MAX_VALUE，将会导致线程数量永远为corePoolSize，再也不会增加，当任务数量陡增时，任务响应时间也将随之陡增。
 *
 * maxPoolSize:最大线程数
 * 当系统负载达到最大值时，核心线程数已无法按时处理完所有任务，这时就需要增加线程。每秒200个任务需要20个线程，那么当每秒达到1000个任务时，则需要(1000-queueCapacity)*(20/200)，即60个线程，可将maxPoolSize设置为60。
 *
 * keepAliveTime:
 * 线程数量只增加不减少也不行。当负载降低时，可减少线程数量，如果一个线程空闲时间达到keepAliveTiime，该线程就退出。默认情况下线程池最少会保持corePoolSize个线程。keepAliveTiime设定值可根据任务峰值持续时间来设定。
 *
 * 以上关于线程数量的计算并没有考虑CPU的情况。若结合CPU的情况，比如，当线程数量达到50时，CPU达到100%，则将maxPoolSize设置为60也不合适，
 * 此时若系统负载长时间维持在每秒1000个任务，则超出线程池处理能力，应设法降低每个任务的处理时间(tasktime)。
 *
 */
@EnableAsync
@Configuration
public class ThreadPoolConfiguration {
    /**
     * 核心线程池数，推荐：根据8020法则，80%时间下的程序每秒任务数(设：tasks=200)，单个线程任务执行时间(设：tasktime=0.1秒)，
     * 程序每秒需要处理任务数/单个线程每秒处理任务数 = (tasks/(1/tasktime)) = tasks*tasktime
     * corePoolSize = Runtime.getRuntime().availableProcessors();
     */
    private final int corePoolSize = 20;
    /**
     * 最大线程池数, 推荐：主要考虑程序最高峰下的任务数(设：maxTasks=1000)，此时任务队列已满。(maxTasks-queueCapacity)*(corePoolSize/maxPoolSize)
     */
    private final int maxPoolSize = 60;
    /**
     * 任务队列的容量 ，推荐：每秒能处理的线程数 * 系统最大允许响应时间(设2秒) = (corePoolSize/tasktime)*responsetime
     */
    private final int queueCapacity = 400;
    /**
     * 非核心线程的存活时间 推荐：设定值可根据任务峰值持续时间来设定。
     */
    private final int keepAliveSeconds = 300;

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程池数
        executor.setMaxPoolSize(maxPoolSize);
        // 最大线程池数
        executor.setCorePoolSize(corePoolSize);
        // 任务队列的容量
        executor.setQueueCapacity(queueCapacity);
        // 非核心线程的存活时间
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 一个执行回调方法的装饰器，主要应用于传递上下文，传递主线程的信息到子线程
        executor.setTaskDecorator(new MdcTaskDecorator());
        // 线程池对拒绝任务(无线程可用)的处理策略
        // - AbortPolicy
        //   用于被拒绝任务的处理程序，它将抛出RejectedExecutionException。- CallerRunsPolicy
        //   用于被拒绝任务的处理程序，它直接在execute方法的调用线程中运行被拒绝的任务。- DiscardOldestPolicy
        //   用于被拒绝任务的处理程序，它放弃最旧的未处理请求，然后重试execute。- DiscardPolicy
        //   用于被拒绝任务的处理程序，默认情况下它将丢弃被拒绝的任务。
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 自定义线程名称, 也可以直接定义 @Bean名称来进行修改
        executor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("app-pool-#%d").build());
        return executor;
    }
}
