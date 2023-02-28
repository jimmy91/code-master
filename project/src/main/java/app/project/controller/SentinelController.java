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


    /**
     * 服务限流后会抛出 BlockException 异常，而 blockHandler 则是用来指定一个函数来处理 BlockException 异常的。 简单点说，该属性用于指定服务限流后的后续处理逻辑。
     * 用于在抛出异常（包括 BlockException）时，提供 fallback 处理逻辑。 fallback 函数可以针对所有类型的异常（除了 exceptionsToIgnore 里面排除掉的异常类型）进行处理。
     * @param type
     * @return
     */
    @PostMapping("/test/{type}")
    @SentinelResource(value = "limit-qps",
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
