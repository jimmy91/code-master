import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
 * @author Jimmy
 */
@Slf4j

// 开启基于注解的缓存 @Cacheable

@SpringBootApplication
// 可以指定要扫描的dao接口类的路径，可以在启动类中添加此注解，可替代@Mapper注解（此模块内dao接口类不用都添加@Mapper注解），两个 * 代表任意个包
// @MapperScan("app.**.mapper")
@ComponentScan("code.**")
public class CoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

}
