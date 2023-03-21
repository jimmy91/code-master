package app.project.controller;

import app.annotation.Idempotent;
import app.contanst.CacheSpaceConstant;
import app.project.entity.SystemDictDataEntity;
import app.project.service.SystemApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import utils.generator.common.dao.req.QueryBO;
import utils.generator.common.dao.vo.CommonResult;
import utils.generator.common.dao.vo.PageResult;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Jimmy
 * @date: 2022-12-30
 * @remark:
 */
@Api(tags = "缓存API")
@RestController
@RequestMapping(value = "/cache")
@Validated
public class SystemApiController {

    @Autowired
    private SystemApiService iSystemDictDataService;

    @PostMapping("list")
    @ApiOperation(value = "分页查询", notes = "字典数据表")
    public CommonResult<PageResult<SystemDictDataEntity>> listSystemDictDataServiceByPage(@Valid @RequestBody QueryBO<SystemDictDataEntity> query) {
        return CommonResult.success(PageResult.getResult(iSystemDictDataService.page(query.getPage(), query.initQueryWrapper())));
    }

    @PostMapping("save")
    @ApiOperation(value = "防止重复提交", notes = "防止重复提交")
    @Idempotent
    public CommonResult<Long> saveSystemDictDataService(@Valid @RequestBody SystemDictDataEntity req) {
        iSystemDictDataService.save(req);
        return CommonResult.success(req.getId());
    }

    // ------------------ @Cache本地缓存 START-------------------

    @PostMapping("update/{id}")
    @ApiOperation(value = "@Cache缓存更新", notes = "修改数据(@Cache缓存更新)")
    @ApiImplicitParam(name = "id", value = "业务主键ID", required = true, dataTypeClass = Long.class)
    @Caching(evict = {
            // 删除命名空间下的所有缓存数据，以:分隔为一个命名空间
            @CacheEvict(cacheNames = CacheSpaceConstant.CACHE_DICT, allEntries = true)
    })
    public CommonResult<Boolean> modifySystemDictDataService(@Valid @RequestBody SystemDictDataEntity req, @PathVariable Long id) {
        req.setId(id);
        return CommonResult.success(iSystemDictDataService.updateById(req));
    }


    @PostMapping("batchQuery")
    @ApiOperation(value = "@Cache缓存查询", notes = "缓存设置固定失效时间")
    @ApiImplicitParam(name = "ids", value = "业务主键ID,多个用逗号分隔.请求参数组装在url后发送", required = true, dataTypeClass = String.class)
    public CommonResult<Map<Long, SystemDictDataEntity>> batchQuerySystemDictDataService(@RequestParam(value = "ids") List<Long> ids) {
        return CommonResult.success(iSystemDictDataService.listMapByIds(ids));
    }

    // ------------------ @Cache本地缓存 END-------------------


    @GetMapping("{id}")
    @ApiOperation(value = "mybatis失效", notes = "每次查询创建一个SqlSession，一级缓存没有生效")
    @ApiImplicitParam(name = "id", value = "业务主键ID", required = true, dataTypeClass = Long.class)
    public CommonResult<SystemDictDataEntity> getSystemDictDataServiceDetails(@PathVariable Long id) {
        SystemDictDataEntity dictDataEntity = null;
        for (int i=0; i< 3; i++){
            dictDataEntity = iSystemDictDataService.getById(id);
        }
        return CommonResult.success(dictDataEntity);
    }


    @GetMapping("{id}/1")
    @ApiOperation(value = "mybatis一级缓存生效", notes = "同一个事务内使用同一个SqlSession，一级缓存生效;<br>" +
            "弊端:因为同一个事务中spring使用的是同一个SqlSession，此时走的是SqlSession的缓存，并没有从数据中查询, 如果有其他线程修改了这条数据,数据不更新。\n" +
            "解决方案\n" +
            "在mybatis的mapper xml里配置清空缓存flushCache设置为true，即：flushCache=\"true\"")
    @ApiImplicitParam(name = "id", value = "业务主键ID", required = true, dataTypeClass = Long.class)
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<SystemDictDataEntity> getSystemDictDataServiceDetails01(@PathVariable Long id) {
        SystemDictDataEntity dictDataEntity = null;
        for (int i=0; i< 3; i++){
            dictDataEntity = iSystemDictDataService.getById(id);
        }
        return CommonResult.success(dictDataEntity);
    }


    @GetMapping("{id}/2")
    @ApiOperation(value = "mybatis二级缓存生效", notes = "mapper缓存")
    @ApiImplicitParam(name = "id", value = "业务主键ID", required = true, dataTypeClass = Long.class)
    public CommonResult<SystemDictDataEntity> getSystemDictDataServiceDetails02(@PathVariable Long id) {
        SystemDictDataEntity dictDataEntity = null;
        for (int i=0; i< 3; i++){
            dictDataEntity = iSystemDictDataService.getTwoLevelCache(id);
        }
        return CommonResult.success(dictDataEntity);
    }

}