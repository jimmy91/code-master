package code.controller;

import code.queue.redis.RedisProducer;
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
@Api(tags = "消息模块")
@RestController
@RequestMapping("/message")
@Validated
public class QueueController {

    @Autowired
    private RedisProducer redisProducer;

    @ApiOperation(value = "发送redis消息", notes="发送即消费")
    @PostMapping("/sendMsg/{topic}/{message}")
    public CommonResult<Boolean> saveRedis(@PathVariable("topic") String topic, @PathVariable("message") String message) {
        redisProducer.sendChannelMess(topic, message);
        return CommonResult.success(true);
    }



}
