package utils.generator;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import utils.generator.generatorTemplate.constant.BasicConstant;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.baomidou.mybatisplus.core.toolkit.StringPool.SLASH;
import static com.baomidou.mybatisplus.core.toolkit.StringPool.UNDERSCORE;


/**
 * @author Jimmy
 * https://www.pudn.com/news/62d140bc864d5c73acb23af8.html
 */
@Slf4j
public class MyBatisPlusGenerator {

    public static String[] TABLES = {
            "mysql_table"
    };

    /**
     * 数据库
     */
    public static String username = "root" ;
    public static String password = "inno@2021" ;
    public static String url = "jdbc:mysql://10.0.99.191:3306/test" ;
    public static String driverClassName = "com.mysql.jdbc.Driver" ;

    public static void main(String[] args) {
        //1. 全局配置
        GlobalConfig config = new GlobalConfig();
        // 是否支持AR模式
        config.setActiveRecord(true)
                .setOpen(false)
                // 作者
                .setAuthor("Jimmy")
                // TODO 生成路径，最好使用绝对路径
                .setOutputDir("E:\\jimmy\\tools_project\\tools\\src\\main\\java")
                // 文件覆盖
                .setFileOverride(true)
                // 主键策略
                .setIdType(IdType.AUTO)
                .setDateType(DateType.ONLY_DATE)
                // 实体属性 Swagger2 注解
                //.setSwagger2(true)
                // 设置生成的service接口的名字的首字母是否为I，默认Service是以I开头的
                .setServiceName("%sService")
                //实体类结尾名称
                .setEntityName("%sEntity")
                //生成基本的resultMap
                .setBaseResultMap(true)
                //不使用AR模式
                .setActiveRecord(false)
                //生成基本的SQL片段
                .setBaseColumnList(true);

        //2. 数据源配置
        DataSourceConfig dsConfig = new DataSourceConfig();
        // TODO 设置数据库类型
        dsConfig.setDbType(DbType.MYSQL)
                .setDriverName(driverClassName)
                .setUrl(url)
                .setUsername(username)
                .setPassword(password);

        //3. 策略配置globalConfiguration中
        StrategyConfig stConfig = new StrategyConfig();

        //全局大写命名
        stConfig.setCapitalMode(true)
                // 数据库表映射到实体的命名策略
                .setNaming(NamingStrategy.underline_to_camel)
                //使用lombok
                .setEntityLombokModel(true)
                //使用restcontroller注解
                .setRestControllerStyle(true)
                // 生成实体类字段注解
                //.setEntityTableFieldAnnotationEnable(true)
                // 逻辑删除、
                // .setLogicDeleteFieldName("deleted")
                // TODO 你自己的父类实体
                // .setSuperEntityClass("utils.generator.common.entity.BaseDO")
                // TODO 生成类的时候排除的字符串(因为这些字段已经在父类中，子类无需再生成)
                // .setSuperEntityColumns(
                //       "creator","create_time","updater","update_time","deleted","tenant_id"  )
                // TODO 生成的表, 支持多表一起生成，以数组形式填写
                // .setInclude("table01", "table02");  // .setInclude(scanner("表名，多个英文逗号分割").split(","));
                .setInclude(TABLES);

        // TODO 包名策略配置
        PackageConfig pkConfig = new PackageConfig();
        pkConfig.setParent("utils.generator.cn.tool.mybatis.module")
                .setMapper("mapper")
                .setService("service")
                .setController("controller")
                .setEntity("entity")
                .setXml("mapper");

        //5. 整合配置
        AutoGenerator ag = new AutoGenerator();
        ag.setGlobalConfig(config)
                .setDataSource(dsConfig)
                .setStrategy(stConfig)
                .setCfg(injectionConfig())
                .setPackageInfo(pkConfig);

        //6. 执行操作
        ag.execute();
        System.out.println("=======  Done 相关代码生成完毕  ========");
    }


    /**
     * 自定义配置
     *
     */
    private static InjectionConfig injectionConfig() {
        return new InjectionConfig() {
            @Override
            public void initMap() {
                // 注入配置
                Map<String, Object> map = new HashMap<>(16);
                //指定表格的名称
                map.put("customerTableName", true);

                for (String tableName : TABLES) {
                    //指定每个类的serialVersionUID
                    long serialVersionUID = UUID.nameUUIDFromBytes(tableName.getBytes()).getLeastSignificantBits();
                    map.put(tableName, Math.abs(serialVersionUID));
                    //计算controller的路径
                    StringBuilder sb = new StringBuilder();
                    if (StrUtil.contains(tableName, UNDERSCORE)) {
                        sb.append(SLASH + tableName.substring(0, tableName.indexOf(UNDERSCORE)));
                        String url = tableName.substring(tableName.indexOf(UNDERSCORE) + 1);
                        sb.append(SLASH + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, url));
                        map.put(tableName + "path", sb.toString());
                        continue;
                    }
                    sb.append(SLASH + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, url));
                    map.put(tableName + "path", sb.toString());
                }
                this.setMap(map);
            }
        };

    }

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
//    public static String scanner(String tip) {
//        Scanner scanner = new Scanner(System.in);
//        StringBuilder help = new StringBuilder();
//        help.append("请输入" + tip + "：");
//        System.out.println(help.toString());
//        if (scanner.hasNext()) {
//            String ipt = scanner.next();
//            if (StringUtils.isNotBlank(ipt)) {
//                return ipt;
//            }
//        }
//        throw new MybatisPlusException("请输入正确的" + tip + "！");
//    }
}