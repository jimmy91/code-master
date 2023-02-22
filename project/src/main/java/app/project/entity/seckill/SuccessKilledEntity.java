package app.project.entity.seckill;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * @description: <秒杀成功明细表实体>
 * @author: Jimmy
 * @date: 2023-02-01
 * @remark:
 */
@Data
@TableName("success_killed")
public class SuccessKilledEntity {

    private static final long serialVersionUID = 5161986964620194454L;

            /**
     * 秒杀商品id
     */
    private Long seckillId;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 状态标示：-1指无效，0指成功，1指已付款
     */
    private Short state;

    private Date createTime;

}