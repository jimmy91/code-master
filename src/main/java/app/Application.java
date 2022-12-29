package app;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Jimmy
 */
@Slf4j
@SpringBootApplication
@MapperScan("app.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }

}
