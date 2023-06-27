package code.controller;

import code.framework.retry.RetryServiceImpl;
import code.framework.screw.ScrewServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.generator.common.dao.vo.CommonResult;

/**
 * @author Jimmy
 */
@Api(tags = "Framework")
@RestController
@RequestMapping("/framework")
@Validated
public class FrameworkController {

    @Autowired
    private ScrewServiceImpl screwService;

    @Autowired
    private RetryServiceImpl retryService;

    @ApiOperation(value = "生成数据库文档", notes = "screw数据库文档")
    @PostMapping("/screw")
    public CommonResult<String> screw() {
       return CommonResult.success(screwService.doDownloadFile()) ;
    }


    @ApiOperation(value = "自动重试", notes = "spring @Retryable重试机制")
    @PostMapping("/retry")
    public CommonResult<String> retry() throws Exception {
        return CommonResult.success(retryService.retryTest("重试了没有")) ;
    }




}
