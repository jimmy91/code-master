package code.trace;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @author Jimmy
 * 如果在 {ThreadPoolConfiguration} 线程池配置中注入装饰器，此类可以删除
 * executor.setTaskDecorator(new MdcTaskDecorator());
 */
@Component("mdc-pool")
public class MdcThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    @Override
    public void execute(Runnable task) {
        // 添加执行回调方法的装饰器，主要应用于传递上下文，传递主线程的信息到子线程
        super.execute(MdcTaskDecorator.decorate(task));
    }
}
