package cn.inno.pala.module.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import cn.inno.pala.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 自由团活动信息表
 * </p>
 *
 * @author Jimmy
 * @since 2022-10-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("task_group_activity")
@ApiModel(value="TaskGroupActivityDO对象", description="自由团活动信息表")
public class TaskGroupActivityDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "通告ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "活动标题")
    @TableField("tittle")
    private String tittle;

    @ApiModelProperty(value = "可报名人数")
    @TableField("need_apply")
    private Integer needApply;

    @ApiModelProperty(value = "活动开始时间")
    @TableField("begin_time")
    private Date beginTime;

    @ApiModelProperty(value = "活动结束时间")
    @TableField("end_time")
    private Date endTime;

    @ApiModelProperty(value = "报名截止时间")
    @TableField("apply_end_time")
    private Date applyEndTime;

    @ApiModelProperty(value = "活动状态，0-未开始 1-报名中 2-报名已满 3-报名已截止 4-活动进行中 5-已结束 6-已取消")
    @TableField("state")
    private Boolean state;

    @ApiModelProperty(value = "活动类型，字典表配置")
    @TableField("activity_type")
    private Boolean activityType;

    @ApiModelProperty(value = "活动地址")
    @TableField("address")
    private String address;

    @ApiModelProperty(value = "活动地址地图经度")
    @TableField("longitude")
    private Double longitude;

    @ApiModelProperty(value = "活动地址地图纬度")
    @TableField("latitude")
    private Double latitude;

    @ApiModelProperty(value = "是否需要付费，0免费 1付费")
    @TableField("need_pay")
    private Boolean needPay;

    @ApiModelProperty(value = "人均付费金额，单位：帕拉币")
    @TableField("amount")
    private Integer amount;

    @ApiModelProperty(value = "付费方式：0-免费 1-男A女免 2-AA 3-我买单")
    @TableField("pay_type")
    private Boolean payType;

    @ApiModelProperty(value = "活动备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "已报名人数")
    @TableField("used_apply")
    private Integer usedApply;

    @ApiModelProperty(value = "队长是否达成用户裂变奖励，0-否，1-已达成，2-已获取奖励")
    @TableField("get_reward")
    private Boolean getReward;

    @ApiModelProperty(value = "审核状态；0-审核中 1-审核成功 2-审核失败")
    @TableField("audit_state")
    private Boolean auditState;

    @ApiModelProperty(value = "审核备注")
    @TableField("audit_remark")
    private String auditRemark;

    @ApiModelProperty(value = "是否启用，1启用 0禁用")
    @TableField("enabled")
    private Boolean enabled;


}
