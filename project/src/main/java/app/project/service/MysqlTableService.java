package app.project.service;

import app.project.entity.MysqlTableEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @description: <这是一个测试表，用于演示不同类型的列服务类>
 * @author: Jimmy
 * @date: 2023-03-21
 * @remark: <>
 */
public interface MysqlTableService extends IService<MysqlTableEntity> {

    MysqlTableEntity getTableData(Integer id);

    MysqlTableEntity saveTableData();
}
