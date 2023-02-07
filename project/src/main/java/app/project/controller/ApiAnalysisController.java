package app.project.controller;

import app.project.service.ApiAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.generator.common.dao.vo.CommonResult;

/**
 * @description:
 * @author: Jimmy
 * @date:
 * @remark:
 */
@Api(tags = "API分析", value = "用于测试场景")
@RestController
@RequestMapping(value = "/analysis")
@Validated
public class ApiAnalysisController {

    @Autowired
    private ApiAnalysisService analysisService;

    @PostMapping("slow")
    @ApiOperation(value = "慢接口")
    public CommonResult<Long> slowApi() {
        Long start = System.currentTimeMillis();
        analysisService.slowApi();
        Long end = System.currentTimeMillis();
        return CommonResult.success(end-start);
    }



}