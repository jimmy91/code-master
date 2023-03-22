package app.project.controller;

import app.project.service.MysqlTableService;
import app.project.vo.MysqlTableVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import utils.generator.common.dao.vo.CommonResult;

/**
 * @description: <这是一个测试表，用于演示不同类型的列前端控制器>
 * @author: Jimmy
 * @date: 2023-03-21
 * @remark:
 */
@Api(tags = "Mysql模块")
@RestController
@RequestMapping(value = "/mysql/table")
@Validated
public class MysqlTableController {
    @Autowired
    private MysqlTableService iMysqlTableService;


    @PostMapping("save")
    @ApiOperation(value = "数据创建", notes="")
    public CommonResult<MysqlTableVo> saveTableData() {
        return CommonResult.success(MysqlTableVo.covert(iMysqlTableService.saveTableData()));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "获取详情", notes="")
    @ApiImplicitParam(name = "id", value = "业务主键ID", required = true, dataTypeClass = Long.class)
    public CommonResult<MysqlTableVo> getTableData(@PathVariable Integer id) {
        return CommonResult.success(MysqlTableVo.covert(iMysqlTableService.getTableData(id)));
    }



}