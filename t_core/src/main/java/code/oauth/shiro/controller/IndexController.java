package code.oauth.shiro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author Jimmy
 * @date 2020/7/9 0009
 */
@Controller
public class IndexController {

	@GetMapping("index")
	public String index(Model model) {
		System.out.println("跳转到主页");
		return "index"; // 要带着后缀就要用redirect
	}

	@GetMapping("hello")
	public String hello(Model m) {
		m.addAttribute("now", DateFormat.getDateTimeInstance().format(new Date()));
		return "hello";
	}

}
