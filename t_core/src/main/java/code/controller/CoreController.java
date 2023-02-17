package code.controller;

import code.queue.redis.RedisSender;
import code.queue.redis.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.generator.common.dao.vo.CommonResult;

/**
 * @author Jimmy
 */
@Api(tags = "Core模块")
@RestController
@RequestMapping("/api")
@Validated
public class CoreController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisSender redisSender;

    @ApiOperation(value = "发送redis消息", notes="")
    @PostMapping("/sendMsg/{topic}/{message}")
    public CommonResult<Boolean> saveRedis(@PathVariable("topic") String topic, @PathVariable("message") String message) {
        redisSender.sendChannelMess(topic, message);
        return CommonResult.success(true);
    }



}
