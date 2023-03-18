package code.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.generator.common.dao.vo.CommonResult;

/**
 * @author jimmy
 * @date
 */
@Api(tags = "SaToken模块")
@RestController
@RequestMapping("oauth/token")
public class SaTokenController {
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @return
	 */
	@PostMapping("login")
	@ApiOperation("用户登录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户Id", defaultValue = "1001"),
			@ApiImplicitParam(name = "username", value = "用户名", defaultValue = "jimmy"),
			@ApiImplicitParam(name = "password", value = "密码", defaultValue = "123456")
	})
	@SaIgnore
	public CommonResult<String> login(Long userId, String username, String password){
		// 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
		if("jimmy".equals(username) && "123456".equals(password)) {
			StpUtil.login(userId);
			return CommonResult.success("登录成功");
		}
		return CommonResult.success("登录失败");
	}

	@PostMapping("isLogin")
	@ApiOperation("校验用户是否登录")
	@SaIgnore
	public CommonResult<Boolean>  isLogin() {
		return CommonResult.success(StpUtil.isLogin());
	}

	@PostMapping("logout")
	@ApiOperation("用户退出")
	public CommonResult<String> logout(){
		StpUtil.logout();
		return CommonResult.success("退出成功");
	}

	@PostMapping("checkLogin")
	@ApiOperation("鉴权访问接口")
	public CommonResult<String> checkLogin(){
		return CommonResult.success("鉴权通过");
	}

	@PostMapping("checkPermission")
	@ApiOperation("授权访问接口")
	@SaCheckPermission("user:add")
	public CommonResult<String> checkPermission(){
		return CommonResult.success("授权通过");
	}


}
