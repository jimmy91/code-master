package app.controller;

import app.entity.TaskGroupActivityDO;
import app.service.TaskGroupActivityService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import utils.generator.common.dao.req.QueryBO;
import utils.generator.common.dao.vo.CommonResult;
import utils.generator.common.dao.vo.PageResult;

import javax.validation.Valid;
import java.util.List;

/**
 * @description: <自由团活动信息表前端控制器>
 * @author: Jimmy
 * @date: 2022-12-29
 * @remark:
 */
@Api(tags = "用户APP - 自由团活动信息表")
@RestController
@RequestMapping(value = "/task/groupActivity")
@Validated
public class TaskGroupActivityController {
    @Autowired
    private TaskGroupActivityService iTaskGroupActivityService;

    @PostMapping("list")
    @ApiOperation(value = "自由团活动信息表分页查询", notes="")
    public CommonResult<PageResult<TaskGroupActivityDO>> listTaskGroupActivityServiceByPage(@Valid @RequestBody QueryBO<TaskGroupActivityDO> query) {
         return CommonResult.success(PageResult.getResult(iTaskGroupActivityService.page(query.getPage() == null ? new Page() : query.getPage(), query.initQueryWrapper())));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "自由团活动信息表获取详情", notes="")
    @ApiImplicitParam(name = "id", value = "业务主键ID", required = true, dataTypeClass = Long.class)
    public CommonResult<TaskGroupActivityDO> getTaskGroupActivityServiceDetails(@PathVariable Long id) {
        return CommonResult.success(iTaskGroupActivityService.getById(id));
    }

    @PostMapping("save")
    @ApiOperation(value = "自由团活动信息表新增数据", notes="")
    public CommonResult<Long> saveTaskGroupActivityService(@Valid @RequestBody TaskGroupActivityDO req) {
        iTaskGroupActivityService.save(req);
        return CommonResult.success(req.getId());
    }

    @PostMapping("update/{id}")
    @ApiOperation(value = "自由团活动信息表修改数据", notes="")
    @ApiImplicitParam(name = "id", value = "业务主键ID", required = true, dataTypeClass = Long.class)
    public CommonResult<Boolean> modifyTaskGroupActivityService(@Valid @RequestBody TaskGroupActivityDO req, @PathVariable Long id) {
        req.setId(id);
        return CommonResult.success(iTaskGroupActivityService.updateById(req));
    }

    @PostMapping("delete")
    @ApiOperation(value = "自由团活动信息表批量删除", notes="")
    @ApiImplicitParam(name = "ids", value = "业务主键ID,多个用逗号分隔.请求参数组装在url后发送", required = true, dataTypeClass = String.class)
    public CommonResult<Boolean>batchRemoveTaskGroupActivityService(@RequestParam(value = "ids") List<Long> ids) {
        return CommonResult.success(iTaskGroupActivityService.removeByIds(ids));
    }

}