package app.project.vo;

import app.annotation.Desensitization;
import app.handler.desensitization.DesensitizationTypeEnum;
import app.project.entity.MysqlTableEntity;
import cn.hutool.core.bean.BeanUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.Year;
import java.util.Date;

/**
 * @description: <这是一个测试表，用于演示不同类型的列实体>
 * @author: Jimmy
 * @date: 2023-03-21
 * @remark:
 * Geometry在数据库和Java中的应用: https://www.jianshu.com/p/76343e71d6f1
 */
@Data
public class MysqlTableVo implements Serializable{

    /**
     * 唯一标识符
     */
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
    //@JsonSerialize(using = JacksonSerializeHandler.class)
    @Desensitization(type = DesensitizationTypeEnum.CHINESE_NAME)
    private String charCol;

    /**
     * VARCHAR类型列
     */
    @Desensitization(type = DesensitizationTypeEnum.CUSTOMER, startInclude = 5, endExclude = 10)
    private String varcharCol;

    /**
     * BINARY类型列
     */
    @Desensitization(type = DesensitizationTypeEnum.CHINESE_NAME)
    private String binaryCol;

    /**
     * VARBINARY类型列
     */
    private String varbinaryCol;

    /**
     * TINYBLOB类型列
     */
    private String tinyblobCol;

    /**
     * TINYTEXT类型列
     */
    private String tinytextCol;

    /**
     * BLOB类型列
     */
    private String blobCol;

    /**
     * TEXT类型列
     */
    private String textCol;

    /**
     * MEDIUMBLOB类型列
     */
    private String mediumblobCol;

    /**
     * MEDIUMTEXT类型列
     */
    private String mediumtextCol;

    /**
     * LONGBLOB类型列
     */
    private String longblobCol;

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
     */
    private MysqlJsonCol jsonCol;

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

    public static MysqlTableVo covert(MysqlTableEntity tableData) {
        MysqlTableVo tableVo = new MysqlTableVo();
        BeanUtil.copyProperties(tableData, tableVo);
        tableVo.setBinaryCol(new String(tableData.getBinaryCol()));
        tableVo.setVarbinaryCol(new String(tableData.getVarbinaryCol()));
        tableVo.setBlobCol(new String(tableData.getBlobCol()));
        tableVo.setLongblobCol(new String(tableData.getLongblobCol()));
        tableVo.setTinyblobCol(new String(tableData.getTinyblobCol()));
        tableVo.setMediumblobCol(null);

        //tableVo.setJsonCol(JSONUtil.toBean(tableData.getJsonCol(), JsonObj.class));

        return tableVo;
    }
}