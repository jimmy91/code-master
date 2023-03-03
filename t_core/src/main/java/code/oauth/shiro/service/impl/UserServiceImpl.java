package code.oauth.shiro.service.impl;

import code.oauth.shiro.mapper.UserDao;
import code.oauth.shiro.pojo.Perms;
import code.oauth.shiro.pojo.User;
import code.oauth.shiro.service.UserService;
import code.oauth.shiro.util.SaltUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jimmy
 * @date 2020/7/12 0012
 * @since
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public void register(User user) {
        //处理业务调用dao
        //1.生成随机盐
        String salt = SaltUtils.getSalt(8);
        //2.将随机盐保存到数据
        user.setSalt(salt);
        //3.明文密码进行md5 + salt + hash散列
        Md5Hash md5Hash = new Md5Hash(user.getPassword(),salt,1024);
        user.setPassword(md5Hash.toHex());
        userDao.insert(user);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.getByUsername(username);
    }

    @Override
    public User findRolesByUserName(String username) {
        return userDao.findRolesByUsername(username);
    }

    @Override
    public List<Perms> findPermsByRoleId(Integer id) {
        return userDao.findPermsByRoleId(id);
    }
}
