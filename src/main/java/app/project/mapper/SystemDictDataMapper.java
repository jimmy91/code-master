package app.project.mapper;

import app.project.entity.SystemDictDataDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @description: <字典数据表Mapper接口>
 * @author: Jimmy
 * @date: 2022-12-29
 * @remark:
 */
@Mapper
public interface SystemDictDataMapper extends BaseMapper<SystemDictDataDO> {

    default Map<Long, List<SystemDictDataDO>> listMapByIds(List<Long> ids){
        return null;
    }

}
