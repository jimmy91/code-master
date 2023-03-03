package app.project.controller;

import app.feign.DemoProviderFeign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.generator.common.dao.vo.CommonResult;


/**
 * @author Jimmy
 */
@Api(tags = "demo测试")
@RestController
@RequestMapping("/data")
public class DemoController {


    @ApiOperation(value = "Feign调用", notes="")
    @GetMapping("/feignDemo")
    public CommonResult<Boolean> feignDemo() {
        DemoProviderFeign.remote.sendMessage("发送消息内容");
        DemoProviderFeign.remote.sendMqTTLMessage();
        return DemoProviderFeign.remote.sendMqXDMessage();
    }
}
