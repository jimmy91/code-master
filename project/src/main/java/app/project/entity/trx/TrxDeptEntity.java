package app.project.entity.trx;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import utils.generator.common.entity.BaseDO;

/**
 * @description: <部门信息表实体>
 * @author: Jimmy
 * @date: 2023-02-10
 * @remark:
 */
@Data
@TableName("trx_dept")
public class TrxDeptEntity {

    private static final long serialVersionUID = 7464038693995208096L;

    /**
     * 部门编号
     */
    @TableId(type = IdType.INPUT)
    private String deptno;

    /**
     * 部门名称
     */
    private String dname;

    /**
     * 位置
     */
    private String loc;

}