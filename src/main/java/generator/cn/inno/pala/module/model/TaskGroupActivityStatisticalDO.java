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
 * 自由团活动统计指标值 
 * </p>
 *
 * @author Jimmy
 * @since 2022-10-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("task_group_activity_statistical")
@ApiModel(value="TaskGroupActivityStatisticalDO对象", description="自由团活动统计指标值 ")
public class TaskGroupActivityStatisticalDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "自由团活动ID")
    @TableField("group_activity_id")
    private Long groupActivityId;

    @ApiModelProperty(value = "评论人次")
    @TableField("comment_num")
    private Integer commentNum;

    @ApiModelProperty(value = "点赞人数")
    @TableField("like_num")
    private Integer likeNum;

    @ApiModelProperty(value = "关注(收藏)人数")
    @TableField("focus_num")
    private Integer focusNum;

    @ApiModelProperty(value = "查看人数/人次")
    @TableField("view_num")
    private Integer viewNum;

    @ApiModelProperty(value = "热度值")
    @TableField("hot_num")
    private Integer hotNum;


}
