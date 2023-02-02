package app.project.mapper;

import app.project.entity.SystemDictDataEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
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
public interface SystemDictDataMapper extends BaseMapper<SystemDictDataEntity> {

    /**
     * 通过ID批量查询
     * @param ids
     * @return
     */
    default Map<Long, SystemDictDataEntity> listMapByIds(List<Long> ids) {
        return CollectionUtils.convertMap(selectBatchIds(ids), SystemDictDataEntity::getId);
    }

}
