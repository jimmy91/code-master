package cn.inno.pala.module.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import cn.inno.pala.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 自由团活动报名记录表
 * </p>
 *
 * @author Jimmy
 * @since 2022-10-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("task_group_activity_apply_record")
@ApiModel(value="TaskGroupActivityApplyRecordDO对象", description="自由团活动报名记录表")
public class TaskGroupActivityApplyRecordDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "自由团活动ID")
    @TableField("group_activity_id")
    private Long groupActivityId;

    @ApiModelProperty(value = "用户ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "报名状态：0-报名成功 1-活动取消(已退款)")
    @TableField("state")
    private Boolean state;

    @ApiModelProperty(value = "用户报名来源，0-普通用户报名 1-新用户裂变报名")
    @TableField("resoure")
    private Boolean resoure;


}
