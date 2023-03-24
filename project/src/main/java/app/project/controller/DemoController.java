package app.project.controller;

import app.feign.DemoProviderFeign;
import code.trace.MdcThreadPoolTaskExecutor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.generator.common.dao.vo.CommonResult;
import utils.redisson.RedissonExpirationListener;


/**
 * @author Jimmy
 */
@Api(tags = "demo测试")
@RestController
@RequestMapping("/demo")
@Slf4j
public class DemoController {

    @Autowired
    MdcThreadPoolTaskExecutor taskExecutor;

    @ApiOperation(value = "Feign调用", notes="")
    @GetMapping("/feignDemo")
    public CommonResult<Boolean> feignDemo() {
        DemoProviderFeign.remote.sendMessage("发送消息内容");
        DemoProviderFeign.remote.sendMqTTLMessage();
        return DemoProviderFeign.remote.sendMqXDMessage();

    }

    @ApiOperation(value = "redisson过期监听", notes="")
    @GetMapping("/redisson")
    public CommonResult<Boolean> redisson() throws Exception {

        RedissonExpirationListener.demoTest();
        return CommonResult.success(true);
    }

    @ApiOperation(value = "Trace链路跟踪", notes="")
    @GetMapping("/trace")
    public CommonResult<Boolean> trace() throws Exception {
        log.info("进入主线程方法");
        Runnable myTask = () -> {
            log.info("执行了异步线程任务开始");
            try {
                RedissonExpirationListener.demoTest();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            log.info("执行了异步线程任务结束");
        };

        // 异步线程
        taskExecutor.execute(myTask);

        // feign调用   // http请求
        feignDemo();

        return CommonResult.success(true);
    }



}
