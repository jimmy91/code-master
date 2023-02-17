package code.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import code.queue.redis.RedisSender;
import code.queue.redis.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import utils.generator.common.dao.vo.CommonResult;

import java.util.HashMap;
import java.util.Map;

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
    @PostMapping("/sendMsg/{channel}/{message}")
    public CommonResult<Boolean> saveRedis(@PathVariable("topic") String channel, @PathVariable("message") String message) {
        redisSender.sendChannelMess(channel, message);
        return CommonResult.success(true);
    }



}
