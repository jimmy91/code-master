package app.config;

import cn.hutool.core.util.StrUtil;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;

/**
 * @author Jimmy
 * @cacheable缓存时间管理器
 */
public class TtlRedisCacheManager extends RedisCacheManager {
    public TtlRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        String[] cells = StrUtil.splitToArray(name, "#");
        name = cells[0];
        if (cells.length > 1) {
            long ttl = Long.parseLong(cells[1]);
            // 根据传参设置缓存失效时间，默认单位是秒
            cacheConfig = cacheConfig.entryTtl(Duration.ofSeconds(ttl));
        }else{
            // 默认缓存一周
            cacheConfig = cacheConfig.entryTtl(Duration.ofDays(7));
        }
        return super.createRedisCache(name, cacheConfig);
    }
}
