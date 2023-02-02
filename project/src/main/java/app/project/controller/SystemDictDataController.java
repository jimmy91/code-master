package app.project.controller;

import app.annotation.Idempotent;
import app.project.entity.SystemDictDataEntity;
import app.project.mapper.SystemDictDataMapper;
import app.project.service.SystemDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import utils.generator.common.dao.req.QueryBO;
import utils.generator.common.dao.vo.CommonResult;
import utils.generator.common.dao.vo.PageResult;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @description: <字典数据表前端控制器>
 * @author: Jimmy
 * @date: 2022-12-30
 * @remark:
 */
@Api(tags = "API模块-字典数据表")
@RestController
@RequestMapping(value = "/system/dictData")
@Validated
public class SystemDictDataController {
    @Autowired
    private SystemDictDataService iSystemDictDataService;

    @PostMapping("list")
    @ApiOperation(value = "分页查询", notes="字典数据表")
    @Idempotent
    public CommonResult<PageResult<SystemDictDataEntity>> listSystemDictDataServiceByPage(@Valid @RequestBody QueryBO<SystemDictDataEntity> query) {
         return CommonResult.success(PageResult.getResult(iSystemDictDataService.page(query.getPage(), query.initQueryWrapper())));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "获取详情", notes="")
    @ApiImplicitParam(name = "id", value = "业务主键ID", required = true, dataTypeClass = Long.class)
    public CommonResult<SystemDictDataEntity> getSystemDictDataServiceDetails(@PathVariable Long id) {
        return CommonResult.success(iSystemDictDataService.getById(id));
    }

    @PostMapping("save")
    @ApiOperation(value = "新增数据", notes="")
    public CommonResult<Long> saveSystemDictDataService(@Valid @RequestBody SystemDictDataEntity req) {
        iSystemDictDataService.save(req);
        return CommonResult.success(req.getId());
    }

    @PostMapping("update/{id}")
    @ApiOperation(value = "修改数据", notes="")
    @ApiImplicitParam(name = "id", value = "业务主键ID", required = true, dataTypeClass = Long.class)
    public CommonResult<Boolean> modifySystemDictDataService(@Valid @RequestBody SystemDictDataEntity req, @PathVariable Long id) {
        req.setId(id);
        return CommonResult.success(iSystemDictDataService.updateById(req));
    }

    @PostMapping("delete")
    @ApiOperation(value = "批量删除", notes="")
    @ApiImplicitParam(name = "ids", value = "业务主键ID,多个用逗号分隔.请求参数组装在url后发送", required = true, dataTypeClass = String.class)
    public CommonResult<Boolean> batchRemoveSystemDictDataService(@RequestParam(value = "ids") List<Long> ids) {
        return CommonResult.success(iSystemDictDataService.removeByIds(ids));
    }

    @Resource
    SystemDictDataMapper mapper;

    @PostMapping("query")
    @ApiOperation(value = "批量查询", notes="")
    @ApiImplicitParam(name = "ids", value = "业务主键ID,多个用逗号分隔.请求参数组装在url后发送", required = true, dataTypeClass = String.class)
    public CommonResult<Map<Long, SystemDictDataEntity>> batchQuerySystemDictDataService(@RequestParam(value = "ids") List<Long> ids) {
        return CommonResult.success(mapper.listMapByIds(ids));
    }


}