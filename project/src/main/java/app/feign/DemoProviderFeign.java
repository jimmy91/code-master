package app.feign;

import org.springframework.web.bind.annotation.GetMapping;
import utils.generator.common.dao.vo.CommonResult;

/**
 * @author Jimmy
 */
//@FeignClient(name = "core-server")
public interface DemoProviderFeign {

    @GetMapping(path = "/message/redis/111/222")
    CommonResult<Boolean> sendMessage();
}

