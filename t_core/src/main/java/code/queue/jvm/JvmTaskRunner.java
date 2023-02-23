package code.queue.jvm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 消费队列,启动便消费
 *
 * @author Jimmy
 */
@Component
@Slf4j
public class JvmTaskRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments var) throws Exception {
        while (true) {
            //进程内队列，进行削峰
            Object queue = JvmQueue.getSeckillQueue().consume();
            if (queue != null) {
                log.info("JVM queue消费队列 {}", queue);
            }
        }
    }
}


