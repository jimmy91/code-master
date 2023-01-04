
import app.Application;
import app.project.service.SystemDictDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * 测试方法
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoTest {

    @Autowired
    private SystemDictDataService dictDataService;

    @Test
    public void testMethod(){
        System.out.println(dictDataService.list());
        System.out.println("成功");
    }

}