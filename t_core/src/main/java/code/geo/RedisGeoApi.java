package code.geo;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jimmy
 */
@Slf4j
@Service
public class RedisGeoApi {

    private final String GEO_KEY = "core:geos";

    @Resource
    private StringRedisTemplate geoRedisTemplate;

    /**
     * 添加坐标点信息
     * redis 命令：geoadd GEO_KEY 116.405285 39.904989 "北京"
     * @param geoInfo
     * @return
     */
    public Long addGeo(GeoInfo geoInfo) {
        return geoRedisTemplate.opsForGeo().add(GEO_KEY, new Point(geoInfo.getLongitude(), geoInfo.getLatitude()), geoInfo.getKey());
    }

    /**
     * 批量添加坐标点信息
     *
     * @param geoInfos
     * @return
     */
    public Long saveGeo(Collection<GeoInfo> geoInfos) {

        log.info("start to save geo user info: {}.", JSON.toJSONString(geoInfos));

        GeoOperations<String, String> ops = geoRedisTemplate.opsForGeo();

        Set<RedisGeoCommands.GeoLocation<String>> locations = new HashSet<>();
        geoInfos.forEach(ci -> locations.add(new RedisGeoCommands.GeoLocation<>(
                ci.getKey(), new Point(ci.getLongitude(), ci.getLatitude())
        )));

        log.info("done to save geo info.");

        return ops.add(GEO_KEY, locations);
    }

    /**
     * 批量获取key的坐标信息
     * redis命令：geopos GEO_KEY 北京
     * @param cities
     * @return
     */
    public List<Point> getGeoPos(String[] cities) {
        GeoOperations<String, String> ops = geoRedisTemplate.opsForGeo();
        return ops.position(GEO_KEY, cities);
    }

    /**
     * 获取两个城市的距离
     * redis命令：geodist GEO_KEY 北京 上海
     * @param key1
     * @param key2
     * @param metric 指定单位:  km:DistanceUnit.KILOMETERS
     * @return
     */
    public Distance getDistance(String key1, String key2, Metric metric) {
        GeoOperations<String, String> ops = geoRedisTemplate.opsForGeo();
        return metric == null ?
                ops.distance(GEO_KEY, key1, key2) : ops.distance(GEO_KEY, key1, key2, metric);
    }

    /**
     * 根据给定的经纬度，返回半径不超过指定距离的元素
     * redis命令：georadius key 116.405285 39.904989 100 km WITHDIST WITHCOORD ASC
     * COUNT 5
     * @param circle
     * @param count 限定返回的记录数
     */
    public GeoResults<RedisGeoCommands.GeoLocation<String>> nearByXY(Circle circle, long count) {
        // includeDistance 包含距离
        // includeCoordinates 包含经纬度
        // sortAscending 正序排序
        // limit 限定返回的记录数
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeDistance().includeCoordinates().sortAscending().limit(count);
        return geoRedisTemplate.opsForGeo().radius(GEO_KEY, circle, args);
    }

    /**
     * 根据指定的地点查询半径在指定范围内的位置
     * redis命令：georadiusbymember key 北京 100 km WITHDIST WITHCOORD ASC COUNT 5
     * @param member 城市名称key
     * @param distance 距离
     * @param count 限定返回的记录数
     */
    public GeoResults<RedisGeoCommands.GeoLocation<String>> nearByPlace(String member, Distance distance, long count) {
        // includeDistance 包含距离
        // includeCoordinates 包含经纬度
        // sortAscending 正序排序
        // limit 限定返回的记录数
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeDistance().includeCoordinates().sortAscending().limit(count);
        return geoRedisTemplate.opsForGeo().radius(GEO_KEY, member, distance, args);
    }

    /**
     * 返回的是geohash值
     * redis命令：geohash key 北京
     */
    public List<String> getGeoHash(String[] cities) {
        GeoOperations<String, String> ops = geoRedisTemplate.opsForGeo();
        return ops.hash(GEO_KEY, cities);
    }

    /**
     * 清除数据
     */
    public void removeAll() {
        geoRedisTemplate.delete(GEO_KEY);
    }


}
