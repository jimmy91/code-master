package app.project.entity.trx;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description: <员工信息表实体>
 * @author: Jimmy
 * @date: 2023-02-10
 * @remark:
 */
@Data
@TableName("trx_emp")
public class TrxEmpEntity {

    private static final long serialVersionUID = 4693036043021360195L;

    /**
     * 员工编号
     */
    @TableId(type = IdType.INPUT)
    private Integer empno;

    /**
     * 员工姓名
     */
    private String ename;

    /**
     * 职业
     */
    private String job;

    /**
     * 上级编号
     */
    private Integer mgr;

    /**
     * 雇佣日期
     */
    private Date hiredate;

    /**
     * 薪水
     */
    private BigDecimal sal;

    /**
     * 奖金
     */
    private BigDecimal comm;

    /**
     * 部门编号
     */
    private String deptno;

}