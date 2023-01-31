package app.project.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author
 * @date 2020-01-13-20:15
 */
@Api(tags = "页面模块")
@Controller
@Slf4j
public class WebController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }


    @RequestMapping("/im")
    public String im() {
        log.info("进入聊天页面.......");
        return "im";
    }

}

