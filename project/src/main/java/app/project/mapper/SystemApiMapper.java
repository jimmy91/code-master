package app.project.mapper;

import app.contanst.CacheSpaceConstant;
import app.project.entity.SystemDictDataEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.Cacheable;
import utils.tools.coll.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @description: <字典数据表Mapper接口>
 * @author: Jimmy
 * @date: 2022-12-30
 * @remark:
 */
@Mapper
public interface SystemApiMapper extends BaseMapper<SystemDictDataEntity> {

    /**
     * 通过ID批量查询
     * @Cacheable文档 https://www.cnblogs.com/coding-one/p/12401630.html
     * @param ids
     * @return
     */
    @Cacheable(cacheNames = CacheSpaceConstant.CACHE_DICT + "#60", key = "'dictIds'" )
    default Map<Long, SystemDictDataEntity> listMapByIds(List<Long> ids) {
        return CollectionUtils.convertMap(selectBatchIds(ids), SystemDictDataEntity::getId);
    }

}
