package app.project.service.impl;

import app.project.entity.SystemDictDataEntity;
import app.project.mapper.SystemDictDataMapper;
import app.project.service.SystemDictDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description: <字典数据表服务实现类>
 * @author: Jimmy
 * @date: 2022-12-30
 * @remark: <>
 */
@Service
public class SystemDictDataServiceImpl extends ServiceImpl<SystemDictDataMapper, SystemDictDataEntity> implements SystemDictDataService {

    @Override
    public Map<Long, SystemDictDataEntity> listMapByIds(List<Long> ids) {
        return this.getBaseMapper().listMapByIds(ids);
    }
}
