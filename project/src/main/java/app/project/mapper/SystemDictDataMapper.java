package app.project.mapper;

import app.project.entity.SystemDictDataDO;
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
public interface SystemDictDataMapper extends BaseMapper<SystemDictDataDO> {

    /**
     * 通过ID批量查询
     * @param ids
     * @return
     */
    default Map<Long, SystemDictDataDO> listMapByIds(List<Long> ids) {
        return CollectionUtils.convertMap(selectBatchIds(ids), SystemDictDataDO::getId);
    }

}
