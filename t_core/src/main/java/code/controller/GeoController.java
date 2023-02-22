package code.controller;

import code.geo.GeoInfo;
import code.geo.RedisGeoApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import utils.generator.common.dao.vo.CommonResult;

import java.util.List;

/**
 * @author Jimmy
 */
@Api(tags = "Geo模块")
@RestController
@RequestMapping("/geo")
@Validated
public class GeoController {

    @Autowired
    private RedisGeoApi redisGeoApi;

    @ApiOperation(value = "保存经纬度信息")
    @PostMapping("/redis/save")
    public CommonResult<Long> saveRedis(@RequestBody GeoInfo geoInfo) {
       return CommonResult.success(redisGeoApi.addGeo(geoInfo)) ;
    }

    @ApiOperation(value = "计算两所城市距离")
    @PostMapping("/redis/distance/{city1}/{city2}")
    public CommonResult<Distance> getDistance( @PathVariable String city1,  @PathVariable String city2) {
        return CommonResult.success(redisGeoApi.getDistance(city1, city2, RedisGeoCommands.DistanceUnit.KILOMETERS)) ;
    }

    @ApiOperation(value = "坐标点距离范围内城市", notes = "根据给定的经纬度，返回半径不超过指定距离的元素")
    @PostMapping("/redis/nearByXY/{distance}")
    public CommonResult<GeoResults<RedisGeoCommands.GeoLocation<String>>> nearByXY(@RequestBody GeoInfo geoInfo, @PathVariable Long distance) {
        Circle c = new Circle(new Point(geoInfo.getLongitude(),geoInfo.getLatitude()), new Distance(distance, RedisGeoCommands.DistanceUnit.KILOMETERS));
        return CommonResult.success(redisGeoApi.nearByXY(c, 1000)) ;
    }


}
