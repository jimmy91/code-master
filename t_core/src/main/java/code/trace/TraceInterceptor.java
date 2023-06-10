package code.trace;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: LogInterceptor
 * @Author Jimmy
 * @Description: 日志拦截。统一在日志中添加链路追踪id
 * http://m.weizhi.cc/tech/detail-336920.html
 * https://www.zhangshengrong.com/p/9MNlDOKvNJ/
 */
@Slf4j
public class TraceInterceptor implements HandlerInterceptor {

    /**
     * 链路中统一的id
     */
    public static final String TRACE_ID = "requestId";
    /**
     * 链路中每个服务的id
     */
    public static final String SPAN_ID = "spanId";

    public static String serviceName;

    public static String getSpanId() {
        if(StrUtil.isEmpty(serviceName)){
            synchronized (TraceInterceptor.class){
                if(StrUtil.isEmpty(serviceName)){
                    Environment environment = SpringUtil.getBean("environment");
                    serviceName =  environment.getProperty("server.servlet.context-path").substring(1);
                }
            }
        }
        return String.format("%s#%s", serviceName, RandomUtil.randomInt(1000, 9999));
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        try {
            // 如果有上层调用就用上层的ID
            // 解决多服务间HTTP调用丢失traceId问题，需要在发送请求时在Request Header中添加requestId，在被调用方添加拦截器获取header中的requestId添加到MDC中。
            String traceId = request.getHeader(TRACE_ID);
            if (StrUtil.isEmpty(traceId)) {
                MDC.put(TRACE_ID, IdUtil.fastSimpleUUID());
            } else {
                MDC.put(TRACE_ID, traceId);
            }
            MDC.put(SPAN_ID, getSpanId());
        } catch (Exception e) {
            log.error("LogInterceptor preHandle catch error msg={}", e.getMessage(), e);
        }

        return true;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest httpServletRequest, @NotNull HttpServletResponse httpServletResponse, @NotNull Object o, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest httpServletRequest, @NotNull HttpServletResponse httpServletResponse, @NotNull Object o, Exception e) {
        //调用结束后删除
        MDC.clear();
    }
}