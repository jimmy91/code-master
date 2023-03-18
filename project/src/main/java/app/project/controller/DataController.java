package app.project.controller;

import app.project.entity.geo.OkGeo;
import app.project.mapper.geo.GeoMapper;
import code.framework.geo.GeoInfo;
import code.framework.geo.RedisGeoApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.generator.common.dao.vo.CommonResult;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;



/**
 * @author Jimmy
 */
@Api(tags = "数据模块")
@RestController
@RequestMapping("/data")
@Validated
public class DataController {


    @Resource
    private GeoMapper geoMapper;

    @Autowired
    private RedisGeoApi redisGeoApi;

    @ApiOperation(value = "初始化geo数据", notes="")
    @PostMapping("/addGeos")
    public CommonResult<Long> addGeos() {

        List<OkGeo> geos = geoMapper.selectList(null);

        List<GeoInfo> infos = new ArrayList<>();
        geos.stream().forEach(p -> {
            String[] xy = p.getGeo().split(" ");
            if(xy.length > 1){
                infos.add(new GeoInfo(p.getName(), Double.valueOf(xy[0]), Double.valueOf(xy[1]))) ;
            }
        });
        return CommonResult.success(redisGeoApi.saveGeo(infos));
    }





}

