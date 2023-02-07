package app.project.service;

import app.project.entity.SystemDictDataEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @description: <字典数据表服务类>
 * @author: Jimmy
 * @date: 2022-12-30
 * @remark: <>
 */
public interface SystemDictDataService extends IService<SystemDictDataEntity> {

    Map<Long, SystemDictDataEntity> listMapByIds(List<Long> ids);

}
