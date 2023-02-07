package app.project.controller;

import app.project.service.ISeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.generator.common.dao.vo.CommonResult;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author
 * @date 2020-01-13-20:15
 */
@Api(tags = "秒杀模块")
@RestController
@RequestMapping("seckill")
@Slf4j
public class SeckillController {

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    /**
     * 创建线程池  调整队列数 拒绝服务
     */
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, corePoolSize + 1, 10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(1000));

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

    @ApiOperation(value = "秒杀一(最low实现)", nickname = "Jimmy")
    @PostMapping("/start")
    public CommonResult start(long seckillId) {
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
    public CommonResult startLock(long seckillId) {
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
    public CommonResult startAopLock(long seckillId) {
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
    public CommonResult startDBPCC_ONE(long seckillId) {
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
    public CommonResult startDPCC_TWO(long seckillId) {
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
    public CommonResult startDBOCC(long seckillId) {
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


    @ApiOperation(value="秒杀柒(进程内队列、消息队列)",nickname="Jimmy")
    @PostMapping("/startQueue")
    public CommonResult startQueue(long seckillId){

        return CommonResult.success(true);
    }

}

