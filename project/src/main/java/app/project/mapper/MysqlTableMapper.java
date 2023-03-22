package app.project.mapper;

import app.project.entity.MysqlTableEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import utils.tools.coll.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @description: <这是一个测试表，用于演示不同类型的列Mapper接口>
 * @author: Jimmy
 * @date: 2023-03-21
 * @remark:
 */
@Mapper
public interface MysqlTableMapper extends BaseMapper<MysqlTableEntity> {
    
    /**
     * 通过ID批量查询
     * @param ids
     * @return
     */
    default Map<Integer, MysqlTableEntity> listMapByIds(List<Long> ids) {
        return CollectionUtils.convertMap(selectBatchIds(ids), MysqlTableEntity::getId);
    }

    MysqlTableEntity getTableData(@Param("id") Integer id);
}
