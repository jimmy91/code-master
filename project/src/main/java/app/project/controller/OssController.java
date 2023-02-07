package app.project.controller;

import app.project.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utils.generator.common.dao.vo.CommonResult;

import java.util.Arrays;

/**
 * @author
 * @date 2020-01-13-20:15
 */
@Api(tags = "文件服务")
@RestController
@RequestMapping("/oss")
@Validated
public class OssController {

    @Autowired
    private OssService ossService;

    @GetMapping("/getSts")
    @ApiOperation("获取token")
    public CommonResult<Object> getSts() {
        return CommonResult.success(ossService.getSTS());
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("文件上传")
    @ApiImplicitParam(name = "file", value = "文件", dataType="file", required = true)
    public CommonResult<String> upload(@RequestParam("file") MultipartFile file) {
        return CommonResult.success(ossService.uploadFile(file));
    }

    @PostMapping(value = "/remove")
    @ApiOperation("文件删除")
    @ApiImplicitParam(name = "objectName", value = "文件名", required = true)
    public CommonResult<Boolean> remove(String objectName) {
        return CommonResult.success(ossService.removeOssFile(Arrays.asList(objectName)));
    }

}

