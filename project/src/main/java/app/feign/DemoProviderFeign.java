package app.feign;

import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import utils.generator.common.dao.vo.CommonResult;

/**
 * @author Jimmy
 */
public interface DemoProviderFeign {

    DemoProviderFeign remote = Feign.builder().decoder(new JacksonDecoder())
            .requestInterceptor(template -> template.header("appid", "appid"))
            .target(DemoProviderFeign.class, "http://localhost:8090/core/");

    @RequestLine("POST /message/redis/111?message={message}")
    CommonResult<Boolean> sendMessage(@Param("message") String message);

    @RequestLine("POST /message/rabbit/ttl")
    CommonResult<Boolean> sendMqTTLMessage();

    @RequestLine("POST /message/rabbit/x-delayed")
    CommonResult<Boolean> sendMqXDMessage();

}

