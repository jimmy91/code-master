package code.trace;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: TraceIdInterceptorConfiguration
 * @Author Jimmy
 * @Description: 注册TraceID的拦截器\生成請求的唯一值
 */
@Configuration
public class TraceInterceptorConfiguration implements WebMvcConfigurer {

    /**
     * 添加拦截器链路的请求路径
     */
    private String[] addPathPatterns = new String[]{"/**"};

    /**
     * 不添加拦截器链路的请求路径
     */
    private String[] excludePathPatterns = new String[]{};

    @Override
    public void addInterceptors(@NotNull InterceptorRegistry interceptorRegistry) {
        // 链路追踪日志拦截器
        TraceInterceptor traceInterceptor = new TraceInterceptor();
        interceptorRegistry
                .addInterceptor(traceInterceptor)
                .addPathPatterns(addPathPatterns)
                .excludePathPatterns(excludePathPatterns);
    }

}