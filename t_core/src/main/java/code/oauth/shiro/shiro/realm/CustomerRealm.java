package code.oauth.shiro.shiro.realm;

import code.oauth.shiro.pojo.Perms;
import code.oauth.shiro.pojo.User;
import code.oauth.shiro.service.UserService;
import code.utils.ApplicationContextUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

/**
 * 自定义realm
 * @author Jimmy
 * @date 2020/7/9 0009
 */
public class CustomerRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	/** 认证方法
	 *
	 * @author Jimmy
	 * @Date: 2020/7/9 0009 11:10
	 * @Param: token
	 * @Return: org.apache.shiro.authc.AuthenticationInfo
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		System.out.println("==========================");
		String principal = (String) token.getPrincipal();
		// 从数据库查询用户信息
		//在工厂中获取service对象
		//UserService userService = (UserService) ApplicationContextUtils.getBean("userService");
		//根据身份信息查询
//		User user = userService.findByUsername(principal);
		User user = userService.findRolesByUserName(principal);

		if (Objects.nonNull(user)) {
			return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()),
					this.getName());
		}

		return null;
	}

	//授权方法
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//		在没有配置缓存的时候,每发起一个请求，就会调用该方法。用户基数大请求多的时候，会对数据库造成很大的压力。
//		所以我们需要配置缓存，将用户信息放在缓存里，从而减小数据库压力。
		System.out.println("进入了授权方法。。。");
		//获取身份信息
//		String primaryPrincipal = (String) principals.getPrimaryPrincipal();
//		System.out.println("调用授权验证: "+primaryPrincipal);
//		//根据主身份信息获取角色 和 权限信息
//
//		//根据主身份信息获取角色 和 权限信息
		//UserService userService = (UserService) ApplicationContextUtils.getBean("userService");
//		User user = userService.findRolesByUserName(primaryPrincipal);

		User user = (User) principals.getPrimaryPrincipal();

		if (!CollectionUtils.isEmpty(user.getRoles())) {
			SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
			user.getRoles().forEach(role -> {
				simpleAuthorizationInfo.addRole(role.getName());
				List<Perms> perms = userService.findPermsByRoleId(role.getId());
				if (!CollectionUtils.isEmpty(perms)) {
					perms.forEach(perms1 -> {
						simpleAuthorizationInfo.addStringPermission(perms1.getName());
					});
				}
			});
			return simpleAuthorizationInfo;
		}

		// simpleAuthorizationInfo.addRoles();
		// simpleAuthorizationInfo.addStringPermission();
		// simpleAuthorizationInfo.addStringPermissions();
		// simpleAuthorizationInfo.addObjectPermission();
		// simpleAuthorizationInfo.addObjectPermissions();

		return null;

	}

}
