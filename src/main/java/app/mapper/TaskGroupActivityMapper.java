package app.mapper;

import app.entity.TaskGroupActivityDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @description: <自由团活动信息表Mapper接口>
 * @author: Jimmy
 * @date: 2022-12-29
 * @remark:
 */
@Mapper
public interface TaskGroupActivityMapper extends BaseMapper<TaskGroupActivityDO> {

    default Map<Long, List<TaskGroupActivityDO>> listMapByIds(List<Long> ids){
        return null;
    }

}
