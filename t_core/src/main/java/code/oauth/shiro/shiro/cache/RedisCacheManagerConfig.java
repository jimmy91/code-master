package code.oauth.shiro.shiro.cache;

import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lwq
 * @date 2020/7/12 0012
 * @since
 */
//@Component
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisCacheManagerConfig {

    private String host;

    private String port;

    // redis
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        // redisManager.setHost(host + ":" + port);
        return redisManager;
    }

    // redis缓存管理器
    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());

        //redis中针对不同用户缓存
        // redisCacheManager.setPrincipalIdFieldName("username");
        //用户权限信息缓存时间
        // redisCacheManager.setExpire(200000);

        return redisCacheManager;
    }

    /**
     * redisSessionDAO
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

}
