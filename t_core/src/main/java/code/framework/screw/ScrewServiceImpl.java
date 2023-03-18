package code.framework.screw;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Screw 一键生成数据库文档
 * @author Jimmy
 */
@Service
@Slf4j
public class ScrewServiceImpl {


    private static final String FILE_OUTPUT_DIR = System.getProperty("java.io.tmpdir") + "db-doc";

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Autowired
    ApplicationContext applicationContext;

    public String doDownloadFile() {
        //数据源  创建HikariConfig配置类
        DataSource dataSourceMysql = getDataSource();
        //该方式数据源下无法获取表说明
        //DataSource dataSourceMysql = applicationContext.getBean(DataSource.class);
        try {
            String fileName = "数据库文档_"+ DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_MINUTE_PATTERN);
            EngineFileType fileType = EngineFileType.HTML;
            // 生成文件配置
            EngineConfig engineConfig = EngineConfig.builder()
                    // 生成文件路径，自己mac本地的地址，这里需要自己更换下路径
                    .fileOutputDir(FILE_OUTPUT_DIR).fileName(fileName)
                    // 打开目录
                    .openOutputDir(false)
                    // 有 HTML、WORD、MD 三种格式的文档。
                    .fileType(fileType)
                    // 生成模板实现,可自定义
                    .produceType(EngineTemplateType.freemarker).build();

            /**
             * 配置想要生成的表+ 配置想要忽略的表
             *
             * @return 生成表配置
             */
            // 忽略表名
            List<String> ignoreTableName = Arrays.asList("a", "test_group");
            // 忽略表前缀，如忽略a开头的数据库表
            List<String> ignorePrefix = Arrays.asList("a", "t");
            // 忽略表后缀
            List<String> ignoreSuffix = Arrays.asList("_test", "czb_");
            ProcessConfig processConfig = ProcessConfig.builder()
                    //根据名称指定表生成
                    //.designatedTableName(Arrays.asList("system_dict_data","system_dict_type"))
                    //根据表前缀生成
                    .designatedTablePrefix(new ArrayList<>())
                    //根据表后缀生成
                    .designatedTableSuffix(new ArrayList<>())
                    //忽略表名
                    .ignoreTableName(ignoreTableName)
                    //忽略表前缀
                    .ignoreTablePrefix(ignorePrefix)
                    //忽略表后缀
                    .ignoreTableSuffix(ignoreSuffix).build();

            // 生成文档配置（包含以下自定义版本号、描述等配置连接）
            Configuration config = Configuration.builder()
                    .title("表结构说明文档")
                    .organization("***研发部")
                    .version("1.0.0")
                    .description("文档信息描述")
                    .dataSource(dataSourceMysql)
                    .engineConfig(engineConfig)
                    .produceConfig(processConfig)
                    .build();
            // 执行生成
            new DocumentationExecute(config).execute();
            String path = FILE_OUTPUT_DIR + File.separator + fileName + fileType.getFileSuffix();
            log.info("数据库文档生成 path= {}", path);
            return path;
        } finally {
            try {
                dataSourceMysql.getConnection().close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @NotNull
    private DataSource getDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        //#设置useInformationSchema 可以获取tables表注释信息 即解决数据库表和列字段有说明、生成文档没有说明
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        DataSource dataSourceMysql = new HikariDataSource(hikariConfig);
        return dataSourceMysql;
    }

}
