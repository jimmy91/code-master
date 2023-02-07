package app;

import io.opentracing.util.GlobalTracer;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

/**
 * @author Jimmy
 */
@Slf4j

// 开启基于注解的缓存 @Cacheable
@EnableCaching

@SpringBootApplication
@MapperScan("app.*.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public void initBean(){
        /**
         * 阿里云跟踪追踪
         */
        // 将manualDemo替换为您的应用名称。
        io.jaegertracing.Configuration config = new io.jaegertracing.Configuration("app-service");
        io.jaegertracing.Configuration.SenderConfiguration sender = new io.jaegertracing.Configuration.SenderConfiguration();
        // 将 <endpoint> 替换为控制台概览页面上相应客户端和地域的接入点。
        sender.withEndpoint("http://tracing-analysis-dc-sz.aliyuncs.com:8000");
        config.withSampler(new io.jaegertracing.Configuration.SamplerConfiguration().withType("const").withParam(1));
        config.withReporter(new io.jaegertracing.Configuration.ReporterConfiguration().withSender(sender).withMaxQueueSize(10000));
        GlobalTracer.register(config.getTracer());
        log.info("阿里云跟踪追踪初始化bean完成");
    }

}
