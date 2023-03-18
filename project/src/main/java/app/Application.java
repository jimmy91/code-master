package app;

import cn.xuyanwu.spring.file.storage.EnableFileStorage;
import io.jaegertracing.Configuration;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @author Jimmy
 */
@Slf4j

// 开启基于注解的缓存 @Cacheable
@EnableCaching
// 表示支持重试功能
@EnableRetry
// 启用 X Spring File Storage
@EnableFileStorage

@SpringBootApplication
// 可以指定要扫描的dao接口类的路径，可以在启动类中添加此注解，可替代@Mapper注解（此模块内dao接口类不用都添加@Mapper注解），两个 * 代表任意个包
@MapperScan({"app.**.mapper", "code.**.mapper"})
// 告诉Spring从哪里找到bea ，定义哪些包需要被扫描。
@ComponentScan({"code.**", "app.**"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // @Bean
    public Tracer initBean(){
        /**
         * 阿里云跟踪追踪
         */
        // 将manualDemo替换为您的应用名称。
        Configuration config = new io.jaegertracing.Configuration("app-service");
        Configuration.SenderConfiguration sender = new Configuration.SenderConfiguration();
        // 将 <endpoint> 替换为控制台概览页面上相应客户端和地域的接入点。
        sender.withEndpoint("http://tracing-analysis-dc-sz.aliyuncs.com:8000");
        config.withSampler(new Configuration.SamplerConfiguration().withType("const").withParam(1));
        config.withReporter(new Configuration.ReporterConfiguration().withSender(sender).withMaxQueueSize(10000));
        GlobalTracer.register(config.getTracer());
        log.info("阿里云跟踪追踪初始化bean完成");
        return config.getTracer();
    }

}
