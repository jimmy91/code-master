package app.project.service.impl;

import app.project.entity.file.FileDetailEntity;
import app.project.mapper.file.FileDetailMapper;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.recorder.FileRecorder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

/**
 * @author Jimmy
 * 实现 FileRecorder 这个接口，把文件信息保存到数据库中
 */
@Service
public class FileDetailServiceImpl extends ServiceImpl<FileDetailMapper, FileDetailEntity> implements FileRecorder {

    /**
     * 保存文件信息到数据库
     */
    @SneakyThrows
    @Override
    public boolean record(FileInfo info) {
        FileDetailEntity detail = BeanUtil.copyProperties(info, FileDetailEntity.class, "attr");

        //这是手动获 取附加属性字典 并转成 json 字符串，方便存储在数据库中
        if (info.getAttr() != null) {
            detail.setAttr(new ObjectMapper().writeValueAsString(info.getAttr()));
        }
        boolean b = save(detail);
        if (b) {
            info.setId(detail.getId().toString());
        }
        return b;
    }

    /**
     * 根据 url 查询文件信息
     */
    @SneakyThrows
    @Override
    public FileInfo getByUrl(String url) {
        FileDetailEntity detail = getOne(new QueryWrapper<FileDetailEntity>().lambda().eq(FileDetailEntity::getUrl, url));
        Assert.notNull(detail, "图片地址不存在");
        FileInfo info = BeanUtil.copyProperties(detail, FileInfo.class, "attr");

        //这是手动获取数据库中的 json 字符串 并转成 附加属性字典，方便使用
        if (StrUtil.isNotBlank(detail.getAttr())) {
            info.setAttr(new ObjectMapper().readValue(detail.getAttr(), Dict.class));
        }
        return info;
    }

    /**
     * 根据 url 删除文件信息
     */
    @Override
    public boolean delete(String url) {
        return remove(new QueryWrapper<FileDetailEntity>().lambda().eq(FileDetailEntity::getUrl, url));
    }
}

