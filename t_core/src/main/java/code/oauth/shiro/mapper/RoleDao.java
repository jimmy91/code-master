package code.oauth.shiro.mapper;

import code.oauth.shiro.pojo.Role;
import code.oauth.shiro.pojo.RoleExample;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleDao  extends MyBatisBaseDao<Role, Integer, RoleExample> {
}