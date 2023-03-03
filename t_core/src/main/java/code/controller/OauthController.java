package code.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.generator.common.dao.vo.CommonResult;

/**
 * @author lwq
 * @date 2020/7/9 0009
 */
@Api(tags = "Oauth模块")
@RestController
@RequestMapping("oauth/shiro")
public class OauthController {
	/**
	 * 用来处理身份认证
	 * @param username
	 * @param password
	 * @return
	 */
	@PostMapping("login")
	@ApiOperation("用户登录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "用户名", defaultValue = "jimmy"),
			@ApiImplicitParam(name = "password", value = "密码", defaultValue = "123456")
	})
	public CommonResult<String> login(String username, String password){
		//获取主体对象
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(new UsernamePasswordToken(username,password));
			return CommonResult.success("登录成功");
		} catch (UnknownAccountException e) {
			e.printStackTrace();
			return CommonResult.success("用户名错误");
		} catch (IncorrectCredentialsException e){
			e.printStackTrace();
			return CommonResult.success("密码错误");
		}
	}

	@PostMapping("logout")
	@ApiOperation("用户退出")
	public  CommonResult<String> logout(){
		//获取主体对象
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return CommonResult.success("退出成功");
	}

}
