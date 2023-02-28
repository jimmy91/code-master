package app.project.controller;

import app.feign.DemoProviderFeign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import utils.generator.common.dao.vo.CommonResult;


/**
 * @author Jimmy
 */
@Api(tags = "demo测试")
@RestController
@RequestMapping("/data")
public class DemoController {

    @Value("${data:error}")
    private String data;

    //@Autowired
    private DemoProviderFeign providerFeign;

    @ApiOperation(value = "获取nacos配置值", notes="")
    @GetMapping("/getNacosConfig")
    @ResponseBody
    public String getNacosConfig() {
        return data;
    }

    @ApiOperation(value = "Feign调用", notes="")
    @GetMapping("/feignDemo")
    @ResponseBody
    public CommonResult<Boolean> feignDemo() {
        return providerFeign.sendMessage();
    }
}
