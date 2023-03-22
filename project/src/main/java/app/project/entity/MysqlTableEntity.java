package app.project.entity;

import app.project.service.impl.MysqlTableServiceImpl;
import app.project.vo.JsonObj;
import cn.hutool.json.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.Year;
import java.util.Date;
import java.util.Map;

/**
 * @description: <这是一个测试表，用于演示不同类型的列实体>
 * @author: Jimmy
 * @date: 2023-03-21
 * @remark:
 * Geometry在数据库和Java中的应用: https://www.jianshu.com/p/76343e71d6f1
 *    1、tinyblob、tinytext最大存储255字节
 * 　　2、blob、text最大存储65K
 * 　　3、mediumblob、mediumtext最大存储16M
 * 　　4、longblob、longtext最大存储4G
 */
@Data
@TableName("mysql_table")
public class MysqlTableEntity implements Serializable{

    private static final long serialVersionUID = 7942459813558162887L;

    /**
     * 唯一标识符
     */
    @TableId(type = IdType.INPUT)
    private Integer id;

    /**
     * TINYINT类型列
     */
    private Integer tinyintCol;

    /**
     * SMALLINT类型列
     */
    private Integer smallintCol;

    /**
     * MEDIUMINT类型列
     */
    private Integer mediumintCol;

    /**
     * INT类型列
     */
    private Integer intCol;

    /**
     * BIGINT类型列
     */
    private Long bigintCol;

    /**
     * FLOAT类型列
     */
    private Float floatCol;

    /**
     * DOUBLE类型列
     */
    private Double doubleCol;

    /**
     * DECIMAL类型列
     */
    private BigDecimal decimalCol;

    /**
     * CHAR类型列
     */
    private String charCol;

    /**
     * VARCHAR类型列
     */
    private String varcharCol;

    /**
     * BINARY类型列
     * 查询可直接转为String, 插入时要特殊处理，故使用byte[]接收，输出时再处理
     * @TableField(typeHandler = BlobTypeHandler.class)
     */
    private byte[] binaryCol;

    /**
     * VARBINARY类型列
     */
    private byte[] varbinaryCol;

    /**
     * TINYBLOB类型列
     * 最大仅255个字符
     */
    private byte[] tinyblobCol;

    /**
     * TINYTEXT类型列
     */
    private String tinytextCol;

    /**
     * BLOB类型列
     * 最大限制到65K字节
     */
    private byte[] blobCol;

    /**
     * TEXT类型列
     */
    private String textCol;

    /**
     * MEDIUMBLOB类型列
     * 限制到16M字节
     */
    private byte[] mediumblobCol;

    /**
     * MEDIUMTEXT类型列
     */
    private String mediumtextCol;

    /**
     * LONGBLOB类型列
     * 可达4GB
     */
    private byte[] longblobCol;

    /**
     * LONGTEXT类型列
     */
    private String longtextCol;

    /**
     * ENUM类型列
     */
    private String enumCol;

    /**
     * SET类型列
     */
    private String setCol;

    /**
     * DATE类型列
     */
    private Date dateCol;

    /**
     * TIME类型列
     */
    private Time timeCol;

    /**
     * DATETIME类型列
     */
    private Date datetimeCol;

    /**
     * TIMESTAMP类型列
     */
    private Date timestampCol;

    /**
     * YEAR类型列
     */
    private Year yearCol;

    /**
     * BOOLEAN类型列
     */
    private Boolean booleanCol;

    /**
     * BIT类型列
     */
    private Boolean bitCol;

    /**
     * JSON类型列
     * https://www.cnblogs.com/liuyiyuan/p/16388360.html
     */
    private String jsonCol;



    /**
     * GEOMETRY类型列
     */
    private String geometryCol;

    /**
     * POINT类型列
     * 点 - 坐标值
     */
    private String pointCol;

    /**
     * LINESTRING类型列
     * 线 - 由一系列点连接而成
     */
    private String linestringCol;

    /**
     * POLYGON类型列
     * 多边形 - 由多条线组成,可以是一个实心平面形，即没有内部边界，也可以有空洞，类似纽扣
     */
    private String polygonCol;

    /**
     * GEOMETRYCOLLECTION类型列
     * 空间数据集合 集合类，可以包括多个点、线、多边形
     */
    private String geometrycollectionCol;

    /**
     * MULTIPOINT类型列
     * 点集合
     */
    private String multipointCol;

    /**
     * MULTILINESTRING类型列
     * 线集合
     */
    private String multilinestringCol;

    /**
     * MULTIPOLYGON类型列
     * 多边形集合
     */
    private String multipolygonCol;

}