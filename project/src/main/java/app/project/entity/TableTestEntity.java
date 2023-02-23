package app.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @description: <测试批量添加实体>
 * @author: Jimmy
 * @date: 2023-02-10
 * @remark:
 */
@Data
@TableName("table_test")
@AllArgsConstructor
public class TableTestEntity {

    private static final long serialVersionUID = 8103158857993387277L;


    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 测试字符串
     */
    private String name;

    /**
     * 测试日期值
     */
    private Date birthday;

    /**
     * 测试double 值
     */
    private Double sal;

    /**
     * 测试枚举值
     */
    private String status;

    /**
     * 测试bool 值
     */
    private Boolean success;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮件
     */
    private String email;

    /**
     * 身份证
     */
    private String idcard;

    /**
     * 家庭住址
     */
    private String address;

    /**
     * 职业
     */
    private String job;

}