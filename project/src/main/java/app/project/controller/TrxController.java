package app.project.controller;

import app.project.service.impl.trx.TrxServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.generator.common.dao.vo.CommonResult;

/**
 * @author Jimmy
 */
@Api(tags = "事务分析", value = "")
@RestController
@RequestMapping(value = "/transactional")
@Slf4j
public class TrxController {

    @Autowired
    private TrxServiceImpl trxService;

    @GetMapping("/noTrx")
    @ApiOperation(value = "无事务(错误)", notes = "无事务单方法内两步提交，无法保证数据事务性")
    public CommonResult<Object> noTrx() {
        try {
            return CommonResult.success(trxService.noTrx());
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommonResult.error(-1, e.getMessage());
        }
    }

    @GetMapping("/trx01")
    @ApiOperation(value = "事务一(常规)", notes = "单方法内添加事务，保证事务数据一致性")
    public CommonResult<Object> trx01() {
        try {
            return CommonResult.success(trxService.trx01());
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommonResult.error(-1, e.getMessage());
        }
    }

    @GetMapping("/trx02")
    @ApiOperation(value = "事务二(子方法异常)", notes = "子方法通过代理方式加入当前事务")
    public CommonResult<Object> trx02() {
        try {
            return CommonResult.success(trxService.trx02());
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommonResult.error(-1, e.getMessage());
        }
    }

    @GetMapping("/trx03")
    @ApiOperation(value = "事务三(事务方法异常)", notes = "")
    public CommonResult<Object> trx03() {
        try {
            return CommonResult.success(trxService.trx03());
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommonResult.error(-1, e.getMessage());
        }
    }

    @GetMapping("/trx04")
    @ApiOperation(value = "事务四(错误-异步线程异常)", notes = "异常线程内异常，线程内操作无法加入当前事务，数据可正常提交")
    public CommonResult<Object> trx04() {
        try {
            return CommonResult.success(trxService.trx04());
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommonResult.error(-1, e.getMessage());
        }
    }

    @GetMapping("/trx05")
    @ApiOperation(value = "事务五(隔离性-读已提交)", notes = "未提交数据，事务外的无法查询到，事务内可查询到该数据")
    public CommonResult<Object> trx05() {
        try {
            return CommonResult.success(trxService.trx05());
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommonResult.error(-1, e.getMessage());
        }
    }

    @GetMapping("/trx06")
    @ApiOperation(value = "事务六(隔离性-脏读)", notes = "注入为提交事务后查询，可查询到未提交的数据")
    public CommonResult<Object> trx06() {
        try {
            return CommonResult.success(trxService.trx06());
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommonResult.error(-1, e.getMessage());
        }
    }

    @GetMapping("/trx07")
    @ApiOperation(value = "事务七(手动提交回滚)", notes = "注入为提交事务后查询")
    public CommonResult<Object> trx07() {
        try {
            return CommonResult.success(trxService.trx07());
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommonResult.error(-1, e.getMessage());
        }
    }


    @GetMapping("/trx08")
    @ApiOperation(value = "事务八(错误-AOP无效事务)", notes = "事务通过AOP代理实现，类内部方法调用，首个入口public方法实现事务才可以开启事务")
    public CommonResult<Object> trx08() {
        try {
            return CommonResult.success(trxService.trx08());
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommonResult.error(-1, e.getMessage());
        }
    }

    @GetMapping("/trx09")
    @ApiOperation(value = "事务九(长链路事务)", notes = "事务会传播")
    public CommonResult<Object> trx09() {
        try {
            return CommonResult.success(trxService.trx09());
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommonResult.error(-1, e.getMessage());
        }
    }

    @GetMapping("/trx10")
    @ApiOperation(value = "事务十(间隙锁)", notes = "范围查询")
    public CommonResult<Object> trx10() {
        try {
            return CommonResult.success(trxService.trx10());
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommonResult.error(-1, e.getMessage());
        }
    }


}
