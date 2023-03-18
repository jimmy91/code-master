package app.project.controller;

import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.FileStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import utils.generator.common.dao.vo.CommonResult;

import java.util.Objects;

/**
 * @author Jimmy
 */
@Api(tags = "文件服务")
@RestController
@RequestMapping("/file")
@Validated
public class FileController {

    /**
     * 注入实列
     */
    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 上传文件，成功返回文件 url
     */
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public CommonResult<String> upload(MultipartFile file) {
        FileInfo fileInfo = fileStorageService.of(file)
                //使用指定的存储平台及三级路径地址
                .setPlatform("aliyun-oss-1").setPath("code/")
                //关联对象id，为了方便管理，不需要可以不写
                .setObjectId("objectId")
                //关联对象类型，为了方便管理，不需要可以不写
                .setObjectType("objectType")
                // 保存一些属性，可以在切面、保存上传记录、自定义存储平台等地方获取使用，不需要可以不写
                //.putAttr("role","admin")
                .upload();  //将文件上传到对应地方
        return CommonResult.success(Objects.isNull(fileInfo) ? "上传失败！" : fileInfo.getUrl());
    }

    /**
     * 上传图片，成功返回文件信息
     * 图片处理使用的是 https://github.com/coobird/thumbnailator
     */
    @ApiOperation("文件压缩上传")
    @PostMapping("/upload-image")
    public CommonResult<FileInfo> uploadImage(MultipartFile file) {
        return CommonResult.success(
                fileStorageService.of(file)
                //使用指定的存储平台及三级路径地址
                .setPlatform("aliyun-oss-1").setPath("code/")
                //将图片大小调整到 1000*1000
                .image(img -> img.size(1000,1000))
                //再生成一张 200*200 的缩略图
                .thumbnail(th -> th.size(200,200))
                .upload()
        );
    }

    /**
     * 上传文件到指定存储平台，成功返回文件信息
     */
    @ApiOperation("文件删除")
    @PostMapping("/delete")
    public CommonResult<Boolean> delete(String url) {
        return CommonResult.success(fileStorageService.delete(url));
    }

}

