package app.project.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author
 * @date 2020-01-13-20:15
 */
@Api(tags = "VIEW模块")
@Controller
@RequestMapping("view")
@Slf4j
public class WebController {

    @ApiOperation(value = "HTML欢迎页")
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @ApiOperation(value = "WEB IM页")
    @GetMapping("/im")
    public String im() {
        log.info("进入聊天页面.......");
        return "im";
    }

}

