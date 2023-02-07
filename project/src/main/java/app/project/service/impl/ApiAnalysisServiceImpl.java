package app.project.service.impl;

import app.project.service.ApiAnalysisService;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Jimmy
 */
@Slf4j
@Service
public class ApiAnalysisServiceImpl implements ApiAnalysisService {

    @Override
    public void slowApi() {
        ThreadUtil.sleep(1000);
        slowApi(RandomUtil.randomLong(500, 2000));
        ThreadUtil.sleep(RandomUtil.randomLong(1000));
    }

    void slowApi(Long t){
        ThreadUtil.sleep(t);
        slowApi2();
    }

    void slowApi2(){
        ThreadUtil.sleep(RandomUtil.randomLong(500, 2000));
    }

}
