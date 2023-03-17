package code.screw;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Screw 一键生成数据库文档
 * @author Jimmy
 */
@Service
@Slf4j
public class ScrewSeviceImpl {

    @Autowired
    ApplicationContext applicationContext;

    public void contextLoads() {
        DataSource dataSourceMysql = applicationContext.getBean(DataSource.class);
        // 生成文件配置
        EngineConfig engineConfig = EngineConfig.builder()
                // 生成文件路径，自己mac本地的地址，这里需要自己更换下路径
                .fileOutputDir("E:\\jimmy")
                // 打开目录
                .openOutputDir(false)
                // 有 HTML、WORD、MD 三种格式的文档。
                .fileType(EngineFileType.HTML)
                // 生成模板实现,可自定义
                .produceType(EngineTemplateType.freemarker).build();
        // 生成文档配置（包含以下自定义版本号、描述等配置连接）

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

        Configuration config = Configuration.builder()
                .title("数据库表结构说明文档")
                .organization("***研发部")
                .version("1.0.3")
                .description("文档信息描述")
                .dataSource(dataSourceMysql)
                .engineConfig(engineConfig)
                .produceConfig(processConfig)
                .build();
        // 执行生成
        new DocumentationExecute(config).execute();
        log.info("数据库文档生成...");
    }


    /**
     * maxAttempts：最大重试次数(包括第一次失败)，默认为3次
     * backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为2000L；
     * multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为2秒，第二次重试为上次间隔时间的1.5倍
     */
    @Retryable(value = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public String retryTest(String text) throws Exception {
        System.out.println(text);
        throw new Exception("手动异常");
    }

    @Recover
    public String recover(Exception e, String text){
        System.out.println("重试失败，回调方法执行！！！！ " +  e.getMessage());
        return text+"，重试成功了。";
    }

}
