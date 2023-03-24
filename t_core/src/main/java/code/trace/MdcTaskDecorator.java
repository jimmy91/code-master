package code.trace;

import org.slf4j.MDC;

import java.util.Map;

/**
 * @ClassName: MdcTaskDecorator
 * @Author Jimmy
 * @Description: 多线程适配器,用于主线程和子线程的上下文传递
 * MDC（Mapped Diagnostic Context，映射调试上下文
 * https://blog.csdn.net/qq_29569183/article/details/111311632
 * https://www.zhangshengrong.com/p/9MNlDOKvNJ/
 */
public class MdcTaskDecorator {

    /**
     * @apiNote 使异步线程池获得主线程的上下文，解决多线程情况下，子线程中打印日志会丢失traceId.
     * @param runnable runnable
     * @return Runnable
     */
    public static Runnable decorate(Runnable runnable) {
        /**
         * 为了线程池中的线程在复用的时候也能获得父线程的MDC中的信息，
         * 子线程第一次初始化的时候没事，因为通过InheritableThreadLocal
         * 已经可以获得MDC中的内容了
         */
        //Map<String, String> map = MDC.getCopyOfContextMap();
        String traceId = MDC.get(TraceInterceptor.TRACE_ID);
        return () -> {
            try {
                // 线程重用的时候，把父线程中的context map内容带入当前线程的context map中，
                // 因为线程已经初始化过了，不会像初始化时那样通过拷贝父线程inheritableThreadLocals到子线程
                // 的inheritableThreadLocals操作来完成线程间context map的传递。
                // 真正执行到这个run方法的时候，已经到了子线程中了，所以要在初始化的时候用
                // MDC.getCopyOfContextMap()来获得父线程contest map，那时候还在父线程域中
                // MDC.setContextMap(map);
                MDC.put(TraceInterceptor.TRACE_ID, traceId);
                MDC.put(TraceInterceptor.SPAN_ID, TraceInterceptor.getSpanId());
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}