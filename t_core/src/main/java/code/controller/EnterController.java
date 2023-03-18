package code.controller;

import code.screw.ScrewSeviceImpl;
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
@Api(tags = "调用入口")
@RestController
@RequestMapping("/invoke")
@Validated
public class EnterController {

    @Autowired
    private ScrewSeviceImpl screwSevice;

    @ApiOperation(value = "一键生成数据库文档")
    @PostMapping("/screw")
    public CommonResult<String> screw() {
       return CommonResult.success( screwSevice.doDownloadFile()) ;
    }


    @ApiOperation(value = "自动重试")
    @PostMapping("/retry")
    public CommonResult<String> retry() throws Exception {
        return CommonResult.success(screwSevice.retryTest("重试了没有")) ;
    }




}
