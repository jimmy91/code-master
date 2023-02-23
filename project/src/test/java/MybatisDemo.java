import app.Application;
import app.project.entity.TableTestEntity;
import app.project.mapper.TableTestMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class MybatisDemo {

    @Resource
    TableTestMapper tableMapper;

    @Test
    public void contextLoads() {
        List<TableTestEntity> batches = tableMapper.selectList(null);
        System.out.println(batches.size());
    }




}
