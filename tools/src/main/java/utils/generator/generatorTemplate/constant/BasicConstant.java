package utils.generator.generatorTemplate.constant;


import com.baomidou.mybatisplus.annotation.DbType;

/**
 * @Package: com.study.generator
 * @description: <表格和数据源配置项>
 * @author: milla
 * @date: 2020/11/12 17:48
 * @UpdateUser: milla
 * @UpdateDate: 2020/11/12 17:48
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public final class BasicConstant {
    /**
     * 作者
     */
    public static String author = "Jimmy" ;
    /**
     * 生成的实体类忽略表前缀: 不需要则置空
     */
    public static String ENTITY_IGNORE_PREFIX = "settings_" ;

    /**
     * 实体类的父类Entity
     */
    public static String SUPER_ENTITY_CLASS = "utils.generator.common.entity.BaseDO" ;

    /**
     * 基础父类继承字段
     */
    public static String[] SUPER_ENTITY_COLUMNS = {
            "creator","create_time","updater","update_time","deleted","tenant_id"
    };


}
