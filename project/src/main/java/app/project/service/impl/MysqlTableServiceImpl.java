package app.project.service.impl;

import app.project.entity.MysqlTableEntity;
import app.project.mapper.MysqlTableMapper;
import app.project.service.MysqlTableService;
import app.project.vo.JsonObj;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalTime;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: <这是一个测试表，用于演示不同类型的列服务实现类>
 * @author: Jimmy
 * @date: 2023-03-21
 * @remark: 几何拓扑套件:https://blog.csdn.net/u010945668/article/details/119910970
 */
@Service
public class MysqlTableServiceImpl extends ServiceImpl<MysqlTableMapper, MysqlTableEntity> implements MysqlTableService {

    @Resource
    private MysqlTableMapper tableMapper;

    @Override
    public MysqlTableEntity getTableData(Integer id) {
        return tableMapper.getTableData(id);
    }

    @Override
    public MysqlTableEntity saveTableData() {
        tableMapper.deleteById(0);
        MysqlTableEntity table = new MysqlTableEntity();
        table.setId(0);
        table.setTinyintCol(9);
        table.setSmallintCol(99);
        table.setMediumintCol(999);
        table.setIntCol(9999);
        table.setBigintCol(99999L);
        table.setFloatCol(0.99F);
        table.setDoubleCol(0.9999D);
        table.setDecimalCol(BigDecimal.TEN);
        table.setCharCol("C");
        table.setVarcharCol("this is varchar data");
        table.setBinaryCol("***binary".getBytes());
        table.setVarbinaryCol("*** this is varbinary data".getBytes());
        table.setTinyblobCol("*** this is tinyblob data".getBytes());
        table.setTinytextCol("this is tinytext data");
        table.setBlobCol("*** this is blob data".getBytes());
        table.setTextCol("this is text data");
        table.setMediumblobCol(FileUtil.readBytes("E:\\jimmy\\tools_project\\doc\\img\\img.png"));
        table.setMediumtextCol("this is mediumtext data");
        table.setLongblobCol("*** this is blob data".getBytes());
        table.setLongtextCol("this is longtext data");
        table.setEnumCol("value1");
        table.setSetCol("value2,value1");
        table.setDateCol(DateUtil.date());
        table.setTimeCol(Time.valueOf(LocalTime.now()));
        table.setDatetimeCol(DateUtil.date());
        table.setTimestampCol(DateUtil.date());
        table.setYearCol(Year.now());
        table.setBooleanCol(true);
        table.setBitCol(false);

        JsonObj jsonObj = new JsonObj();
        jsonObj.setName("姓名");
        jsonObj.setAge(18);
        table.setJsonCol(JSONUtil.toJsonStr(jsonObj));
       /* table.setGeometryCol();
        table.setPointCol();
        table.setLinestringCol();
        table.setPolygonCol();
        table.setGeometrycollectionCol();
        table.setMultipointCol();
        table.setMultilinestringCol();
        table.setMultipolygonCol();*/


        tableMapper.insert(table);
        return table;
    }


}
