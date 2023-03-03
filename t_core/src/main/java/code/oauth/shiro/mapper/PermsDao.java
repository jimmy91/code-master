package code.oauth.shiro.mapper;

import code.oauth.shiro.pojo.Perms;
import code.oauth.shiro.pojo.PermsExample;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermsDao extends MyBatisBaseDao<Perms, Integer, PermsExample> {
}