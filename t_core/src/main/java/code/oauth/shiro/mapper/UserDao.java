package code.oauth.shiro.mapper;

import code.oauth.shiro.pojo.Perms;
import code.oauth.shiro.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.jdbc.Null;

import java.util.List;

@Mapper
public interface UserDao extends MyBatisBaseDao<User, Integer, Null> {
//    int deleteByPrimaryKey(Integer id);
//
//    int insert(User record);
//
//    int insertSelective(User record);
//
//    User selectByPrimaryKey(Integer id);
//
//    int updateByPrimaryKeySelective(User record);
//
//    int updateByPrimaryKey(User record);

    // 根据用户名查询
    User getByUsername(String username);

    //根据用户名查询所有角色
    User findRolesByUsername(String username);
    //根据角色id查询权限集合
    List<Perms> findPermsByRoleId(Integer id);


}