package app.project.service.impl;

import app.project.entity.SystemDictDataEntity;
import app.project.mapper.SystemApiMapper;
import app.project.service.SystemApiService;
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
public class SystemApiServiceImpl extends ServiceImpl<SystemApiMapper, SystemDictDataEntity> implements SystemApiService {

    @Override
    public Map<Long, SystemDictDataEntity> listMapByIds(List<Long> ids) {
        return this.getBaseMapper().listMapByIds(ids);
    }

    @Override
    public SystemDictDataEntity getTwoLevelCache(Long id) {
        return this.getBaseMapper().getTwoLevelCache(id);
    }
}
