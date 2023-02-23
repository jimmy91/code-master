package app.project.entity.seckill;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import utils.generator.common.entity.BaseDO;

import java.util.Date;

/**
 * @description: <秒杀库存表实体>
 * @author: Jimmy
 * @date: 2023-02-01
 * @remark:
 */
@Data
@TableName("seckill")
public class SeckillEntity {

    private static final long serialVersionUID = 6403020623743723763L;

            @TableId(value = "seckill_id", type = IdType.AUTO)
    /**
     * 商品库存id
     */
    private Long seckillId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 库存数量
     */
    private Long number;

    /**
     * 秒杀开启时间
     */
    private Date startTime;

    /**
     * 秒杀结束时间
     */
    private Date endTime;

    /**
     * 版本号
     */
    private Integer version;

}