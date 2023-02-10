package app.project.mapper.trx;

import app.project.entity.trx.TrxEmpEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description: <员工信息表Mapper接口>
 * @author: Jimmy
 * @date: 2023-02-10
 * @remark:
 */
@Mapper
public interface TrxEmpMapper extends BaseMapper<TrxEmpEntity> {

    TrxEmpEntity selectEmpByNo(Long no);

}
