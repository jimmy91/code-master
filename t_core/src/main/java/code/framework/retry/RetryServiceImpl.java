package code.framework.retry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * 重试
 * @author Jimmy
 */
@Service
@Slf4j
public class RetryServiceImpl {

    /**
     * maxAttempts：最大重试次数(包括第一次失败)，默认为3次
     * backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为2000L；
     * multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为2秒，第二次重试为上次间隔时间的1.5倍
     */
    @Retryable(value = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public String retryTest(String text) throws Exception {
        System.out.println(text);
        throw new Exception("手动异常");
    }

    /**
     * 重试失败后回调入口，回调的返回结果会返回给前端应用
     * @param e
     * @param text
     * @return
     */
    @Recover
    public String recover(Exception e, String text){
        System.out.println("重试失败，回调方法执行！！！！ " +  e.getMessage());
        return "重试失败了，执行了最终回调方法";
    }

}
