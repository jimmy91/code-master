package app.project.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.exception.ExceptionUtil;
import utils.generator.common.dao.vo.CommonResult;

/**
 * @author Jimmy
 */
@Api(tags = "服务治理")
@RestController
@RequestMapping("sentinel")
@Slf4j
public class SentinelController {


    @PostMapping("/test/{type}")
    @SentinelResource(value = "测试",
            fallback = "fallback", fallbackClass = ExceptionUtil.class,
            blockHandler = "handleException", blockHandlerClass = ExceptionUtil.class
    )
    public CommonResult<String> test(@PathVariable("type") Integer type) {

        if (type == 1) {
            throw new IllegalArgumentException("非法参数异常");
        }
        return CommonResult.success("success");
    }
}
