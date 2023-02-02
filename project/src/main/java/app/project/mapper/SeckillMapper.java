package app.project.mapper;

import app.project.entity.SeckillEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description: <秒杀库存表Mapper接口>
 * @author: Jimmy
 * @date: 2023-02-01
 * @remark:
 */
@Mapper
public interface SeckillMapper extends BaseMapper<SeckillEntity> {


    SeckillEntity selectForUpdate(Long seckillId);

}
