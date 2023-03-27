package app.project.controller;

import app.project.entity.seckill.SuccessKilledEntity;
import app.project.service.ISeckillService;
import code.queue.jvm.JvmQueue;
import code.queue.redis.RedisProducer;
import code.queue.redis.RedisSubListenerConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import utils.generator.common.dao.vo.CommonResult;
import utils.redisson.RedissLockUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Jimmy
 */
@Api(tags = "秒杀模块")
@RestController
@RequestMapping("seckill")
@Slf4j
public class SeckillController {

    /**
     * 创建线程池  调整队列数 拒绝服务
     */
    @Autowired
    private ThreadPoolTaskExecutor executor;
    /**
     * 最大并发数
     */
    private static final int MAXOCCURS = 1000;

    /**
     * 休眠时间
     */
    private static final int SLEEP = 10 * 1000;

    @Autowired
    private ISeckillService seckillService;

    @Autowired
    private RedisProducer redisProducer;


    @ApiOperation(value = "秒杀一(最low实现)", nickname = "Jimmy")
    @PostMapping("/start")
    public CommonResult start(@RequestParam(defaultValue = "1000") long seckillId) {
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        Integer seckillCount = null;
        log.info("开始秒杀一(会出现超卖)");
        CountDownLatch latch = new CountDownLatch(MAXOCCURS);
        Long start = System.currentTimeMillis();
        for (int i = 0; i < MAXOCCURS; i++) {
            final long userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    CommonResult result = seckillService.startSeckil(killId, userId);
                    log.info("用户:{}{}", userId, result.getMsg());
                    latch.countDown();
                }
            };
            executor.execute(task);
        }
        try {
            latch.await(SLEEP, TimeUnit.MILLISECONDS);
            seckillCount = seckillService.getSeckillCount(seckillId);
            log.info("一共秒杀出{}件商品，耗时{}毫秒", seckillCount, (System.currentTimeMillis() - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CommonResult.success(seckillCount);
    }

    @ApiOperation(value = "秒杀二(程序锁)", nickname = "Jimmy")
    @PostMapping("/startLock")
    public CommonResult startLock(@RequestParam(defaultValue = "1000") long seckillId) {
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        Integer seckillCount = null;
        log.info("开始秒杀二(正常)");
        CountDownLatch latch = new CountDownLatch(MAXOCCURS);
        Long start = System.currentTimeMillis();
        for (int i = 0; i < MAXOCCURS; i++) {
            final long userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    CommonResult result = seckillService.startSeckilLock(killId, userId);
                    log.info("用户:{}{}", userId, result.getMsg());
                    latch.countDown();
                }
            };
            executor.execute(task);
        }
        try {
            latch.await(SLEEP, TimeUnit.MILLISECONDS);
            seckillCount = seckillService.getSeckillCount(seckillId);
            log.info("一共秒杀出{}件商品，耗时{}毫秒", seckillCount, (System.currentTimeMillis() - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CommonResult.success(seckillCount);
    }

    @ApiOperation(value = "秒杀三(AOP程序锁)", nickname = "Jimmy")
    @PostMapping("/startAopLock")
    public CommonResult startAopLock(@RequestParam(defaultValue = "1000") long seckillId) {
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        Integer seckillCount = null;
        log.info("开始秒杀三(正常)");
        CountDownLatch latch = new CountDownLatch(MAXOCCURS);
        Long start = System.currentTimeMillis();
        for (int i = 0; i < MAXOCCURS; i++) {
            final long userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    CommonResult result = seckillService.startSeckilAopLock(killId, userId);
                    log.info("用户:{}{}", userId, result.getMsg());
                    latch.countDown();
                }
            };
            executor.execute(task);
        }
        try {
            latch.await(SLEEP, TimeUnit.MILLISECONDS);
            seckillCount = seckillService.getSeckillCount(seckillId);
            log.info("一共秒杀出{}件商品，耗时{}毫秒", seckillCount, (System.currentTimeMillis() - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CommonResult.success(seckillCount);
    }

    @ApiOperation(value = "秒杀四(数据库悲观锁)", nickname = "Jimmy")
    @PostMapping("/startDBPCC_ONE")
    public CommonResult startDBPCC_ONE(@RequestParam(defaultValue = "1000") long seckillId) {
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        Integer seckillCount = null;
        log.info("开始秒杀四(正常)");
        CountDownLatch latch = new CountDownLatch(MAXOCCURS);
        Long start = System.currentTimeMillis();
        for (int i = 0; i < MAXOCCURS; i++) {
            final long userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    CommonResult result = seckillService.startSeckilDBPCC_ONE(killId, userId);
                    log.info("用户:{}{}", userId, result.getMsg());
                    latch.countDown();
                }
            };
            executor.execute(task);
        }
        try {
            latch.await(SLEEP, TimeUnit.MILLISECONDS);
            seckillCount = seckillService.getSeckillCount(seckillId);
            log.info("一共秒杀出{}件商品，耗时{}毫秒", seckillCount, (System.currentTimeMillis() - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CommonResult.success(seckillCount);
    }

    @ApiOperation(value = "秒杀五(数据库锁最优实现)", nickname = "Jimmy")
    @PostMapping("/startDPCC_TWO")
    public CommonResult startDPCC_TWO(@RequestParam(defaultValue = "1000") long seckillId) {
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        Integer seckillCount = null;
        log.info("开始秒杀五(正常、数据库锁最优实现)");
        CountDownLatch latch = new CountDownLatch(MAXOCCURS);
        Long start = System.currentTimeMillis();
        for (int i = 0; i < MAXOCCURS; i++) {
            final long userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    CommonResult result = seckillService.startSeckilDBPCC_TWO(killId, userId);
                    log.info("用户:{}{}", userId, result.getMsg());
                    latch.countDown();
                }
            };
            executor.execute(task);
        }
        try {
            latch.await(SLEEP, TimeUnit.MILLISECONDS);
            seckillCount = seckillService.getSeckillCount(seckillId);
            log.info("一共秒杀出{}件商品，耗时{}毫秒", seckillCount, (System.currentTimeMillis() - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CommonResult.success(seckillCount);
    }

    @ApiOperation(value = "秒杀六(数据库乐观锁)", nickname = "Jimmy")
    @PostMapping("/startDBOCC")
    public CommonResult startDBOCC(@RequestParam(defaultValue = "1000") long seckillId) {
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        Integer seckillCount = null;
        log.info("开始秒杀六(正常、数据库锁最优实现)");
        CountDownLatch latch = new CountDownLatch(MAXOCCURS);
        Long start = System.currentTimeMillis();
        for (int i = 0; i < MAXOCCURS; i++) {
            final long userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    CommonResult result = seckillService.startSeckilDBOCC(killId, userId, 1);
                    log.info("用户:{}{}", userId, result.getMsg());
                    latch.countDown();
                }
            };
            executor.execute(task);
        }
        try {
            latch.await(SLEEP, TimeUnit.MILLISECONDS);
            seckillCount = seckillService.getSeckillCount(seckillId);
            log.info("一共秒杀出{}件商品，耗时{}毫秒", seckillCount, (System.currentTimeMillis() - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CommonResult.success(seckillCount);
    }


    @ApiOperation(value = "秒杀柒(队列削峰)", notes = "queue仅适应于单机环境，集群环境可使用redis、mq")
    @PostMapping("/startQueue")
    public CommonResult startQueue(@RequestParam(defaultValue = "1000") long seckillId) {
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        Integer seckillCount = null;
        log.info("开始秒杀柒(正常)");
        Long start = System.currentTimeMillis();
        for (int i = 0; i < MAXOCCURS; i++) {
            final long userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    SuccessKilledEntity kill = new SuccessKilledEntity();
                    kill.setSeckillId(killId);
                    kill.setUserId(userId);
                    try {
                        // 进入队列，无法进行队列直接失败
                        Boolean flag = JvmQueue.getSeckillQueue().produce(kill);
                        if (flag) {
                            log.info("用户:{}{}", kill.getUserId(), "秒杀成功");
                        } else {
                            log.info("用户:{}{}", userId, "秒杀失败");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        log.info("用户:{}{}", userId, "秒杀失败");
                    }
                }
            };
            executor.execute(task);
        }
        seckillCount = seckillService.getSeckillCount(seckillId);
        log.info("一共秒杀出{}件商品，耗时{}毫秒", seckillCount, (System.currentTimeMillis() - start));

        return CommonResult.success("队列消费未实现");
    }

    @ApiOperation(value = "集群秒杀捌(Rediss分布式锁)")
    @PostMapping("/startRedisLock")
    public CommonResult startRedisLock(@RequestParam(defaultValue = "1000") long seckillId) {
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        Integer seckillCount = null;
        CountDownLatch latch = new CountDownLatch(MAXOCCURS);
        Long start = System.currentTimeMillis();
        for (int i = 0; i < MAXOCCURS; i++) {
            final long userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    // 尝试获取锁，最多等待3秒，上锁以后20秒自动解锁（实际项目中推荐这种，以防出现死锁）、这里根据预估秒杀人数，设定自动释放锁时间
                    if (RedissLockUtil.tryLock("seckill:lock:" + seckillId, 3, 20)) {
                        try {
                            CommonResult result = seckillService.startSeckil(killId, userId);
                            log.info("用户:{}{}", userId, result.getMsg());
                        } finally {
                            // 一定要释放锁
                            RedissLockUtil.unlock("seckill:lock:" + seckillId);
                        }
                    }
                }
            };
            executor.execute(task);
        }
        try {
            latch.await(SLEEP, TimeUnit.MILLISECONDS);
            seckillCount = seckillService.getSeckillCount(seckillId);
            log.info("一共秒杀出{}件商品，耗时{}毫秒", seckillCount, (System.currentTimeMillis() - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CommonResult.success(seckillCount);
    }

    @ApiOperation(value = "集群秒杀玖(Redis分布式队列)", notes = "Redis分布式队列-订阅监听,")
    @PostMapping("/startRedisQueue")
    public CommonResult startRedisQueue(@RequestParam(defaultValue = "1000") long seckillId) {
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        Integer seckillCount = null;
        Long start = System.currentTimeMillis();
        for (int i = 0; i < MAXOCCURS; i++) {
            final long userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    //思考如何返回给用户信息ws
                    redisProducer.sendChannelMess(RedisSubListenerConfig.TOPIC, killId + ";" + userId);
                }
            };
            executor.execute(task);
        }
        seckillCount = seckillService.getSeckillCount(seckillId);
        log.info("一共秒杀出{}件商品，耗时{}毫秒", seckillCount, (System.currentTimeMillis() - start));

        return CommonResult.success("队列消费未实现");
    }


}

