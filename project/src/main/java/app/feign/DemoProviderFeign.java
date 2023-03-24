package app.feign;

import code.trace.TraceInterceptor;
import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import org.slf4j.MDC;
import utils.generator.common.dao.vo.CommonResult;

/**
 * @author Jimmy
 */
public interface DemoProviderFeign {

    DemoProviderFeign remote = Feign.builder().decoder(new JacksonDecoder())
            .requestInterceptor(template -> {
                        template.header("appid", "appid");
                        // Feign添加header注入
                        template.header(TraceInterceptor.TRACE_ID, MDC.get(TraceInterceptor.TRACE_ID));
                    }
            )
            .target(DemoProviderFeign.class, "http://localhost:8090/core/");

    @RequestLine("POST /message/redis/111?message={message}")
    CommonResult<Boolean> sendMessage(@Param("message") String message);

    @RequestLine("POST /message/rabbit/ttl")
    CommonResult<Boolean> sendMqTTLMessage();

    @RequestLine("POST /message/rabbit/x-delayed")
    CommonResult<Boolean> sendMqXDMessage();

}

