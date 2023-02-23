package utils.ratelimter;

import com.google.common.util.concurrent.RateLimiter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Jimmy
 * 令牌桶算法:
 * 思考令牌桶的实现可以以下特点。
 * 1s / 阈值（QPS）  = 令牌添加时间间隔。
 * 桶的容量等于限流的阈值，令牌数量达到阈值时，不再添加。
 * 可以适应流量突发，N 个请求到来只需要从桶中获取 N 个令牌就可以继续处理。
 * 有启动过程，令牌桶启动时桶中无令牌，然后按照令牌添加时间间隔添加令牌，若启动时就有阈值数量的请求过来，会因为桶中没有足够的令牌而触发拒绝策略，不过如 RateLimiter 限流工具已经优化了这类问题。
 */
public class GoogleRateLimiter {

    public static void main(String[] args) throws InterruptedException {
        // qps 2  单机限流算法，分布式：如阿里开源的 Sentinel
        RateLimiter rateLimiter = RateLimiter.create(2);
        Thread.sleep(1000);
        for (int i = 0; i < 10; i++) {
            String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
            System.out.println(time + ":" + rateLimiter.tryAcquire());
            Thread.sleep(250);
        }
    }
}
