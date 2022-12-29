package app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import utils.generator.common.entity.BaseDO;

import java.util.Date;

/**
 * @description: <自由团活动信息表实体>
 * @author: Jimmy
 * @date: 2022-12-29
 * @remark:
 */
@Data
@TableName("task_group_activity")
public class TaskGroupActivityDO extends BaseDO {

    private static final long serialVersionUID = 7443146591582183079L;

            @TableId(value = "id", type = IdType.AUTO)
    /**
     * 通告ID
     */
    private Long id;

    /**
     * 活动标题
     */
    private String tittle;

    /**
     * 可报名人数
     */
    private Integer needApply;

    /**
     * 活动开始时间
     */
    private Date beginTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 报名截止时间
     */
    private Date applyEndTime;

    /**
     * 活动状态，1-报名中 2-报名已满 3-报名已截止 4-活动进行中 5-已结束 6-已取消
     */
    private Boolean state;

    /**
     * 活动类型，字典表配置
     */
    private Boolean activityType;

    /**
     * 活动地址
     */
    private String address;

    /**
     * 活动地址地图经度
     */
    private Double longitude;

    /**
     * 活动地址地图纬度
     */
    private Double latitude;

    /**
     * 是否需要付费，0免费 1付费
     */
    private Boolean needPay;

    /**
     * 男士付费金额, 人民币 单位：分
     */
    private Integer boyAmount;

    /**
     * 女士付费金额, 人民币 单位：分
     */
    private Integer girlAmount;

    /**
     * 允许最迟退款时间: -1-随时可退、0-不可退、m-提前m小时可退
     */
    private Integer allowRefundHour;

    /**
     * 付费方式：0-免费 1-男A女免 2-AA 3-我买单
     */
    private Boolean payType;

    /**
     * 活动备注
     */
    private String remark;

    /**
     * 已报名人数
     */
    private Integer usedApply;

    /**
     * 队长是否达成用户裂变奖励，0-否，1-已达成，2-已获取奖励
     */
    private Boolean getReward;

    /**
     * 审核状态；0-审核中 1-审核成功 2-审核失败
     */
    private Boolean auditState;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 取消备注
     */
    private String cancelRemark;

    /**
     * 是否启用，1启用 0禁用
     */
    private Boolean enabled;

    /**
     * 环信群组ID
     */
    private String emGroupId;

    /**
     * 环信群组状态 0-未创建 1-已创建 2-已注销
     */
    private Boolean emGroupState;

    /**
     * 队长结算状态：0-待结算 1-已结算
     */
    private Boolean settlementState;

    /**
     * 结算时间
     */
    private Date settlementTime;

}