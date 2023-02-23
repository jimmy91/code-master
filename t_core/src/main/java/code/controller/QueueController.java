package code.controller;

import cn.hutool.core.map.MapUtil;
import code.queue.rabbit.RabbitProducer;
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

import java.util.Map;

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

    @Autowired
    private RabbitProducer rabbitProducer;

    @ApiOperation(value = "发送redis消息", notes="发送即消费")
    @PostMapping("/redis/{topic}/{message}")
    public CommonResult<Boolean> sendRedisMess(@PathVariable("topic") String topic, @PathVariable("message") String message) {
        redisProducer.sendChannelMess(topic, message);
        return CommonResult.success(true);
    }

    @ApiOperation(value = "rabbit延时(TTL)", notes="")
    @PostMapping("/rabbit/ttl")
    public CommonResult<Boolean> sendRabbitTTLMess() {
        rabbitProducer.sendTTLMess("消息内容");
        return CommonResult.success(true);
    }


    @ApiOperation(value = "rabbit延时(x-delayed)", notes="")
    @PostMapping("/rabbit/x-delayed")
    public CommonResult<Boolean> sendX_DelayMess() {
        rabbitProducer.sendX_DelayMess("消息内容");
        return CommonResult.success(true);
    }




}
